
package com.practicalHadoop.lucene;


import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

import com.practicalHadoop.lucene.indexing.HBaseIndexReader;
import com.practicalHadoop.lucene.indexing.HBaseIndexWriter;
import com.practicalHadoop.lucene.indexing.support.SpatialReaderFilter;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;
import com.practicalHadoop.lucene.spatial.GeometricShapeHelper;
import com.practicalHadoop.lucene.spatial.SpatialDocument;
import com.practicalHadoop.lucene.spatial.geometry.BoundingBox;
import com.practicalHadoop.lucene.spatial.geometry.GeometricShape;
import com.practicalHadoop.lucene.spatial.geometry.Point;

/**
 * Test suite for the HBase Lucene class. <p>
 *
 * @author  BL - Boris Lublinsky
 */

public class HBaseLuceneGeoSpatialTests extends TestCase {

    private static final String indexName = "id";
    private static final Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_33);

    private HBaseIndexWriter indexWriter;
    private int id = 0;
    
    @Before
	public void setUp() throws Exception {
		super.setUp();
        LuceneHBaseInitializer.init();
        indexWriter = new HBaseIndexWriter(indexName, analyzer);
        id = 1;
	}
    
	private void addPoint(HBaseIndexWriter writer, String name, double lat, double lon) throws Exception{

		Point p = new Point(lat, lon);
		Document doc = new Document();
		Field Id1 = new Field("id", Integer.toString(id++), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS);       
		doc.add(new Field("name", name,Field.Store.YES, Field.Index.ANALYZED));
		doc.add(Id1);
		SpatialDocument sDoc = new SpatialDocument(doc, p);
		writer.addSpatialDocument(sDoc, 8, 16);
	}

	private void addData(HBaseIndexWriter writer) throws Exception {
		addPoint(writer,"McCormick &amp; Schmick's Seafood Restaurant",38.9579000,-77.3572000);
		addPoint(writer,"Jimmy's Old Town Tavern",38.9690000,-77.3862000);
		addPoint(writer,"Ned Devine's",38.9510000,-77.4107000);
		addPoint(writer,"Old Brogue Irish Pub",38.9955000,-77.2884000);
		addPoint(writer,"Alf Laylah Wa Laylah",38.8956000,-77.4258000);
		addPoint(writer,"Sully's Restaurant &amp; Supper",38.9003000,-77.4467000);
		addPoint(writer,"TGIFriday",38.8725000,-77.3829000);
		addPoint(writer,"Potomac Swing Dance Club",38.9027000,-77.2639000);
		addPoint(writer,"White Tiger Restaurant",38.9027000,-77.2638000);
		addPoint(writer,"Jammin' Java",38.9039000,-77.2622000);
		addPoint(writer,"Potomac Swing Dance Club",38.9027000,-77.2639000);
		addPoint(writer,"WiseAcres Comedy Club",38.9248000,-77.2344000);
		addPoint(writer,"Glen Echo Spanish Ballroom",38.9691000,-77.1400000);
		addPoint(writer,"Whitlow's on Wilson",38.8889000,-77.0926000);
		addPoint(writer,"Iota Club and Cafe",38.8890000,-77.0923000);
		addPoint(writer,"Hilton Washington Embassy Row",38.9103000,-77.0451000);
		addPoint(writer,"HorseFeathers, Bar & Grill", 39.01220000000001, -77.3942);
		addPoint(writer,"Marshall Island Airfield",7.06, 171.2);
		addPoint(writer, "Wonga Wongue Reserve, Gabon", -0.546562,9.459229);
		addPoint(writer,"Midway Island",25.7, -171.7);
		addPoint(writer,"North Pole Way",55.0, 4.0);
		indexWriter.close();
	}

    @Test
	public void testPointsInBB() throws Exception {

		addData(indexWriter);

		HBaseIndexReader reader = new HBaseIndexReader();
		BoundingBox bb = new BoundingBox(38, -78, 39, -77);		
		SpatialReaderFilter rf = GeometricShapeHelper.buildReaderFilter(bb, 10, 16);
		reader.setReaderFilter(rf);
		IndexSearcher searcher = new IndexSearcher(reader);

		Query q1 = new TermQuery(new Term("name", "Old"));
		
		TopDocs hits = searcher.search(q1, 50);
		
		System.out.println("Found " + hits.totalHits);
		for(ScoreDoc d : hits.scoreDocs){
			Document doc = reader.document(d.doc);
			GeometricShape shape = GeometricShapeHelper.getGeometry(doc);
			System.out.println("Found " + doc);
		}		
	}
}