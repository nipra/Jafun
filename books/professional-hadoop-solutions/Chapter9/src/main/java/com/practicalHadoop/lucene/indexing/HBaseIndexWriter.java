package com.practicalHadoop.lucene.indexing;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermVectorOffsetInfo;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.practicalHadoop.lucene.document.TermDocument;
import com.practicalHadoop.lucene.cache.DocumentInfo;
import com.practicalHadoop.lucene.cache.LuceneDocumentCache;
import com.practicalHadoop.lucene.cache.LuceneDocumentNormsCache;
import com.practicalHadoop.lucene.cache.LuceneIndexCache;
import com.practicalHadoop.lucene.document.TermDocumentFrequency;
import com.practicalHadoop.lucene.document.TermsOffset;
import com.practicalHadoop.lucene.document.singleField;
import com.practicalHadoop.lucene.tables.DocumentCollector;
import com.practicalHadoop.lucene.tables.IndexCollector;
import com.practicalHadoop.lucene.tables.IndexKey;
import com.practicalHadoop.lucene.spatial.GeometricShapeHelper;
import com.practicalHadoop.lucene.spatial.SpatialDocument;
import com.practicalHadoop.lucene.spatial.geometry.CartesianTier;

public class HBaseIndexWriter {

	private static final Log LOG = LogFactory.getLog(HBaseIndexReader.class);
	private static List<Integer> EMPTY_TERM_POSITIONS = Arrays.asList(0);
	private final Analyzer _analyzer;    // how to analyze text
	private Similarity similarity = Similarity.getDefault(); // how to normalize;

	private final String _indexName;
	private DocumentCollector _dCollector;
	private IndexCollector _iCollector;

	public HBaseIndexWriter(String indexName) {

		_indexName = indexName;
		_analyzer = null;
		_dCollector = new DocumentCollector();
		_iCollector = new IndexCollector();
	}

	public HBaseIndexWriter(String indexName, Analyzer a) {

		_indexName = indexName;
		_analyzer = a;
		_dCollector = new DocumentCollector();
		_iCollector = new IndexCollector();
	}

	public void addDocument(Document doc) throws CorruptIndexException, Exception {
		addDocument(doc, _analyzer);
	}

	public void addSpatialDocument(SpatialDocument doc, int sTier, int endTier) throws Exception {
		addSpatialDocument(doc, _analyzer, sTier, endTier);
	}

//	@SuppressWarnings("unchecked")
	public void addDocument(Document doc, Analyzer analyzer) throws Exception {

		if(analyzer == null)
			throw new RuntimeException("Analyzer is not specified");
		// check for special field name
		String docId = getDocID(doc);
		
		DocumentInfo ds = new DocumentInfo();
		int position = 0;

		for (Fieldable field : (List<Fieldable>) doc.getFields()) {
			
			if (field.isIndexed()){
				if (!field.isTokenized()) {

					// Untokenized fields go in without a termPosition

					IndexKey indexKey = new IndexKey(field.name(), field.stringValue());
					TermDocument td = new TermDocument();
					td.docFrequency = 1;
					td.docPositions = EMPTY_TERM_POSITIONS;
					TermDocumentFrequency termd = new TermDocumentFrequency();
					termd.docFrequency = 1;
					termd.docPositions = EMPTY_TERM_POSITIONS;
					ds.addTermDocument(indexKey, termd);
					LuceneIndexCache.addDocument(_iCollector,indexKey.getIndex(), 1, 0, docId, td);
				}else{
					posTerms result = processTokenizedField(field, analyzer, doc, docId, position);
					Map<String, freqPos> allTermInformation = result.getTermFreq();
					position = result.getPosition();
					
					// Store all terms for the field
					for(String term : allTermInformation.keySet()){
						IndexKey indexKey = new IndexKey(field.name(), term);
						freqPos termInfo = allTermInformation.get(term);
						TermDocument td = new TermDocument();
						td.docFrequency = termInfo.getFreq();
						td.docPositions = termInfo.getPositions();
						TermDocumentFrequency termd = new TermDocumentFrequency();
						termd.docFrequency = termInfo.getFreq();
						termd.docPositions = termInfo.getPositions();
						termd.docOffsets = termInfo.getOffsets();
						ds.addTermDocument(indexKey, termd);
						LuceneIndexCache.addDocument(_iCollector,indexKey.getIndex(), 1, 0, docId, td);
					}
				}          
			}
			// Stores each field
			if (field.isStored()) {

				Object value = field.isBinary() ? field.getBinaryValue() : field.stringValue();
				singleField fd = new singleField();
				fd.binary = field.isBinary();
				fd.data = value;
				ds.addField(field.name(),fd);
			}
		}

		// Finally, Store this document
		LuceneDocumentCache.addDocument(_dCollector, docId, ds);
	}
	
//	@SuppressWarnings("unchecked")
	public void addSpatialDocument(SpatialDocument doc, Analyzer analyzer, int sTier, int endTier) throws Exception {

		if(analyzer == null)
			throw new RuntimeException("Analyzer is not specified");
		// check for special field name
		String docId = getDocID(doc.getDocument());

		Map<CartesianTier, List<Long>> tierBoxes = new HashMap<CartesianTier, List<Long>>();
		Map<CharSequence, List<Long>> tBoxes = new HashMap<CharSequence, List<Long>>();		
		for (int i = sTier; i <= endTier; i++) {
			//Store boxes
			CartesianTier tier = new CartesianTier(i);
			List<Long> boxes = tier.getTierValues(doc.getShape());
			tierBoxes.put(tier, boxes);
			tBoxes.put(Integer.toString(i), boxes);
		}
		
		GeometricShapeHelper.addGeometry(doc.getDocument(), tierBoxes, doc.getShape());

		DocumentInfo ds = new DocumentInfo();
		int position = 0;

		for (Fieldable field : (List<Fieldable>) doc.getDocument().getFields()) {
			
			if (field.isIndexed()){
				if (!field.isTokenized()) {

					// Untokenized fields go in without a termPosition

					IndexKey indexKey = new IndexKey(field.name(), field.stringValue());
					TermDocument td = new TermDocument();
					td.docFrequency = 1;
					td.docPositions = EMPTY_TERM_POSITIONS;
					TermDocumentFrequency termd = new TermDocumentFrequency();
					termd.docFrequency = 1;
					termd.docPositions = EMPTY_TERM_POSITIONS;
					LuceneIndexCache.addDocument(_iCollector, indexKey.getIndex(), 1, 0, docId, td);
					if(!indexKey.getField().equals(GeometricShapeHelper.GEOMETRY_TYPE_FIELD)
							&& !indexKey.getField().equals(GeometricShapeHelper.GEOMETRY_FIELD)
							&& !indexKey.getField().startsWith(CartesianTier.DEFALT_FIELD_PREFIX)){
						for(Map.Entry<CartesianTier, List<Long>> entry : tierBoxes.entrySet()){
							int level = entry.getKey().getTierLevel();
							LuceneIndexCache.addDocument(_iCollector,indexKey.getIndex(), level, entry.getValue(), docId, td);
							for(Long box : entry.getValue()){
								IndexKey ik = new IndexKey(indexKey.getIndex(), level, box);
								ds.addTermDocument(ik, termd);
							}
						}
					}
					ds.addTermDocument(indexKey, termd);
				}else{

					posTerms result = processTokenizedField(field, analyzer, doc.getDocument(), docId, position);
					Map<String, freqPos> allTermInformation = result.getTermFreq();
					position = result.getPosition();
					
					// Store all terms for the field
					for(String term : allTermInformation.keySet()){
						IndexKey indexKey = new IndexKey(field.name(), term);
						freqPos termInfo = allTermInformation.get(term);
						TermDocument td = new TermDocument();
						td.docFrequency = termInfo.getFreq();
						td.docPositions = termInfo.getPositions();
						TermDocumentFrequency termd = new TermDocumentFrequency();
						termd.docFrequency = termInfo.getFreq();
						termd.docPositions = termInfo.getPositions();
						termd.docOffsets = termInfo.getOffsets();
						LuceneIndexCache.addDocument(_iCollector, indexKey.getIndex(), 1, 0, docId, td);
						if(!indexKey.getField().equals(GeometricShapeHelper.GEOMETRY_TYPE_FIELD)
								&& !indexKey.getField().equals(GeometricShapeHelper.GEOMETRY_FIELD)
								&& !indexKey.getField().startsWith(CartesianTier.DEFALT_FIELD_PREFIX)){
							for(Map.Entry<CartesianTier, List<Long>> entry : tierBoxes.entrySet()){
								int level = entry.getKey().getTierLevel();
								LuceneIndexCache.addDocument(_iCollector, indexKey.getIndex(), level, entry.getValue(), docId, td);
								for(Long box : entry.getValue()){
									IndexKey ik = new IndexKey(indexKey.getIndex(), level, box);
									ds.addTermDocument(ik, termd);
								}
							}
						}
						ds.addTermDocument(indexKey, termd);
					}
				}          
			}
			// Stores each field
			if (field.isStored()) {

				Object value = field.isBinary() ? field.getBinaryValue() : field.stringValue();
				singleField fd = new singleField();
				fd.binary = field.isBinary();
				fd.data = value;
				ds.addField(field.name(),fd);
			}
		}

		// Finally, Store this document
		LuceneDocumentCache.addDocument(_dCollector, docId, ds);
	}
	
	private String getDocID(Document doc){
		
		String docId = null;
		Field idField = doc.getField(_indexName);
		if((idField != null) && !(idField.isBinary()))
			docId = idField.stringValue();

		if (docId == null)
			docId = Long.toHexString((long) (System.nanoTime() + (Math.random() * System.nanoTime())));
		return docId;
	}
	
	private posTerms processTokenizedField(Fieldable field, 
			Analyzer analyzer, Document doc, String id, int position)throws Exception {
		
		TokenStream tokens = field.tokenStreamValue();

		if (tokens == null) {
			tokens = analyzer.tokenStream(field.name(), new StringReader(field.stringValue()));
		}

		// collect term information per field
		Map<String, freqPos> allTermInformation = new HashMap<String, freqPos>();

		int lastOffset = 0;
		if (position > 0) {
			position += analyzer.getPositionIncrementGap(field.name());
		}

		// Build the termPositions vector for all terms

		tokens.reset(); // reset the TokenStream to the first token

		// set up token attributes we are working on

		// offsets
		OffsetAttribute offsetAttribute = null;
		if (field.isStoreOffsetWithTermVector())
			offsetAttribute = (OffsetAttribute) tokens.addAttribute(OffsetAttribute.class);

		// positions
		PositionIncrementAttribute posIncrAttribute = /*null;
		if (field.isStorePositionWithTermVector())
			posIncrAttribute =*/ (PositionIncrementAttribute) tokens.addAttribute(PositionIncrementAttribute.class);

		CharTermAttribute termAttribute = (CharTermAttribute) tokens.addAttribute(CharTermAttribute.class);

		// store normalizations of field per term per document rather
		// than per field.
		// this adds more to write but less to read on other side
		int tokensInField = 0;

		while (tokens.incrementToken()) {
			tokensInField++;
			String term = new String(termAttribute.buffer(), 0, termAttribute.length());

			// fetch all collected information for this term
			freqPos termInfo = allTermInformation.get(term);

			if (termInfo == null) {
				termInfo = new freqPos();
				allTermInformation.put(term, termInfo);
			}
			else
				termInfo.incrementFrequency();

			// position vector
//			if (field.isStorePositionWithTermVector()) {
				termInfo.addPosition(position);
				position += posIncrAttribute.getPositionIncrement();
//			}

			// term offsets
			if (field.isStoreOffsetWithTermVector()) {
				TermVectorOffsetInfo offset = new TermVectorOffsetInfo
				(lastOffset + offsetAttribute.startOffset(), lastOffset + offsetAttribute.endOffset());
				termInfo.addOffset(offset);
			}
		}
		tokens.close();
		
		// Store Norms
		if (!field.getOmitNorms()) {
			float norm = doc.getBoost();
			norm *= field.getBoost();
//			norm *= similarity.lengthNorm(field.name(), tokensInField);
			byte bnorm = similarity.encodeNormValue(norm);
			LuceneDocumentNormsCache.storeNorm(field.name(), id, bnorm);
		}
		return new posTerms(position, allTermInformation);
	}
	
	public void deleteDocument(String docID) throws Exception {

		LuceneDocumentCache.deleteDocument(docID);
	}

	public void deleteDocuments(Query query) throws CorruptIndexException, Exception {

		HBaseIndexReader reader = new HBaseIndexReader();
		IndexSearcher searcher = new IndexSearcher(reader);

		TopDocs results = searcher.search(query, 1000);

		for (int i = 0; i < results.totalHits; i++) {
			ScoreDoc doc = results.scoreDocs[i];

			String docId = reader.getDocID(doc.doc);
			LuceneDocumentCache.deleteDocument(docId);
		}
	}

//	@SuppressWarnings("unchecked")
	public void deleteDocuments(Term term) throws Exception {

		deleteDocuments(new TermQuery(term));
	}

	public void updateDocument(Term updateTerm, Document doc, Analyzer analyzer) throws Exception {

		deleteDocuments(updateTerm);
		addDocument(doc, analyzer);

	}

	public int docCount() {

		return LuceneDocumentCache.getNumDocs();
	}

	public boolean isAutoCommit() {
		return true;
	}

	public void setAutoCommit(boolean autoCommit) {

	}

	public void commit() {
		_dCollector.finalize();
		_iCollector.finalize();
	}

	public void close() {
		_dCollector.finalize();
		_iCollector.finalize();
	}

	private static class freqPos{

		private int _freq;
		private List<Integer> _positions;
		private List<TermsOffset> _offsets;

		public freqPos(){
			_freq = 1;
			_positions = new ArrayList<Integer>();
			_offsets = new ArrayList<TermsOffset>();
		}

		public void incrementFrequency(){

			_freq++;
		}

		public void addPosition(int p){

			_positions.add(p);
		}

		public void addOffset(TermVectorOffsetInfo offset){

			TermsOffset tOffset = new TermsOffset();
			tOffset.startOffset = offset.getStartOffset();
			tOffset.endOffset = offset.getEndOffset();
			_offsets.add(tOffset);
		}

		public int getFreq() {
			return _freq;
		}

		public List<Integer> getPositions() {

			return _positions;
		}

		public List<TermsOffset> getOffsets() {

			return _offsets;
		}
	}
	
	private static class posTerms{
		
		private int _position;
		Map<String, freqPos> _termFreq;
		
		public posTerms(int pos, Map<String, freqPos> map){
			
			_position = pos;
			_termFreq = map;
		}

		public int getPosition() {
			return _position;
		}

		public Map<String, freqPos> getTermFreq() {
			return _termFreq;
		}
	}
}