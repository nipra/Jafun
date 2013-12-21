
package com.practicalHadoop.lucene;


import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.util.Version;
import org.junit.Before;

import com.practicalHadoop.lucene.indexing.HBaseIndexReader;
import com.practicalHadoop.lucene.indexing.HBaseIndexWriter;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import org.junit.Test;

/**
 * Test suite for the HBase Lucene class. <p>
 *
 * @author  BL - Boris Lublinsky
 */

public class HBaseLuceneTests extends TestCase {

	private static final String indexName = "id";
	private static final Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_33);
	private static final String text = "this is an example value foobar foobar";

	private HBaseIndexWriter indexWriter;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		LuceneHBaseInitializer.init();
		indexWriter = new HBaseIndexWriter(indexName, analyzer);
	}

	@Test
	public void testWriter() throws Exception {

		Document doc1 = new Document();
		Field f = new Field("key", text, Field.Store.YES, Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS);
		Field Id1 = new Field("id", "1", Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS);       
		doc1.add(f);
		doc1.add(Id1);

		indexWriter.addDocument(doc1, analyzer);

		Document doc2 = new Document();
		Field f2 = new Field("key", "this is another example", Field.Store.YES, Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS);
		Field Id2 = new Field("id", "2", Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS);       
		doc2.add(f2);
		doc2.add(Id2);
		doc2.setBoost(3.0f);
		indexWriter.addDocument(doc2, analyzer);


		// Index 100 documents to test order
		for (int i = 30; i >= 20; i--) {
			Document doc = new Document();
			doc.add(new Field("key", "sort this", Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("date", "test" + i, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("id", Integer.toString(i), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			indexWriter.addDocument(doc, analyzer);
		}

		// Unicode doc
		Document d3 = new Document();
		d3.add(new Field("key", "\u5639\u563b", Field.Store.YES, Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));
		d3.add(new Field("key", "samefield", Field.Store.YES, Field.Index.ANALYZED));
		d3.add(new Field("url", "http://www.google.com", Field.Store.YES, Field.Index.NOT_ANALYZED));
		d3.add(new Field("id", "500", Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
		indexWriter.addDocument(d3, analyzer);
		indexWriter.close();

	}

	@Test
	public void testUnicode() throws Exception {
		IndexReader indexReader = new HBaseIndexReader();
		IndexSearcher searcher = new IndexSearcher(indexReader);

		QueryParser qp = new QueryParser(Version.LUCENE_33, "key", analyzer);
		Query q = qp.parse("+key:\u5639\u563b");

		TopDocs docs = searcher.search(q, 10);

		assertEquals(1, docs.totalHits);

		Document doc = searcher.doc(docs.scoreDocs[0].doc);

		assertNotNull(doc.getField("key"));
	}

	@Test
	public void testMultiValuedFields() throws Exception {

		IndexReader indexReader = new HBaseIndexReader();
		IndexSearcher searcher = new IndexSearcher(indexReader);

		QueryParser qp = new QueryParser(Version.LUCENE_33, "key", analyzer);
		Query q = qp.parse("+key:samefield");

		TopDocs docs = searcher.search(q, 10);

		assertEquals(1, docs.totalHits);

		Document doc = searcher.doc(docs.scoreDocs[0].doc);

		Field[] fields = doc.getFields("key");
		String[] tests = new String[]{"\u5639\u563b","samefield"};

		assertEquals(2,fields.length);

		for(int i=0; i<fields.length; i++){
			assertEquals(tests[i],fields[i].stringValue());
		}

	}

	@Test
	public void testKeywordField() throws Exception {
		IndexReader indexReader = new HBaseIndexReader();
		IndexSearcher searcher = new IndexSearcher(indexReader);


		TermQuery tq = new TermQuery(new Term("url", "http://www.google.com"));
		TopDocs topDocs = searcher.search(tq, 10);

		assertEquals(topDocs.totalHits,1);

	}

	@Test
	public void testSearch() throws Exception {

		IndexReader indexReader = new HBaseIndexReader();
		IndexSearcher searcher = new IndexSearcher(indexReader);

		QueryParser qp = new QueryParser(Version.LUCENE_33, "key", analyzer);
		Query q = qp.parse("+key:another");

		TopDocs docs = searcher.search(q, 10);

		assertEquals(1, docs.totalHits);

		Document doc = searcher.doc(docs.scoreDocs[0].doc);

		assertNotNull(doc.getField("key"));
	}

	@Test
	public void testScore() throws Exception {

		IndexReader indexReader = new HBaseIndexReader();
		IndexSearcher searcher = new IndexSearcher(indexReader);

		QueryParser qp = new QueryParser(Version.LUCENE_33, "key", analyzer);
		Query q = qp.parse("+key:example");

		TopDocs docs = searcher.search(q, 10);

		assertEquals(2, docs.totalHits);

		Document doc1 = searcher.doc(docs.scoreDocs[0].doc);
		Document doc2 = searcher.doc(docs.scoreDocs[1].doc);

		String fld = doc1.getField("key").stringValue();
		// Highest scoring doc should be the one with higher boost
		assertEquals(fld, "this is another example");
	} 
}
