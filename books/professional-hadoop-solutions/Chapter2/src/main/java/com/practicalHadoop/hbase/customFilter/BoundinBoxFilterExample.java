package com.practicalHadoop.hbase.customFilter;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;


public class BoundinBoxFilterExample {

	public static void main(String[] args) throws Exception {

		// Variables
		String tName = "testFilter";
		String fName = "famA";
		String c1Name = "col1";
		String c2Name = "col2";
		byte[] famA = Bytes.toBytes(fName);
		byte[] coll1 = Bytes.toBytes(c1Name);  
		byte[] coll2 = Bytes.toBytes(c2Name);  
		
		// Configure connection
		Configuration.addDefaultResource("SandBox_Cloud_Config.xml");
		Configuration conf = HBaseConfiguration.create(new Configuration());

		// Rebuild the table
		HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
	    HTableDescriptor desc;
	    
        if(hBaseAdmin.tableExists(tName)){
        	hBaseAdmin.disableTable(tName);
        	hBaseAdmin.deleteTable(tName);
        }
        desc = new HTableDescriptor(tName);
	    desc.addFamily(new HColumnDescriptor(fName));
	    hBaseAdmin.createTable(desc);
		
		HTable hTable = new HTable(conf,tName);  

		// Put data
		Put put = new Put(Bytes.toBytes("b"));
		put.add(famA, coll1, Bytes.toBytes("0.,0."));
		put.add(famA, coll2, Bytes.toBytes("hello world!"));
		hTable.put(put);
		put = new Put(Bytes.toBytes("d"));
		put.add(famA, coll1, Bytes.toBytes("0.,1."));
		put.add(famA, coll2, Bytes.toBytes("hello hbase!"));
		hTable.put(put);
		put = new Put(Bytes.toBytes("f"));
		put.add(famA, coll1, Bytes.toBytes("0.,2."));
		put.add(famA, coll2, Bytes.toBytes("blahblah"));
		hTable.put(put);
		
		Scan scan = new Scan(Bytes.toBytes("a"), Bytes.toBytes("z"));
		scan.addColumn(famA, coll1);  
		scan.addColumn(famA, coll2);  

		WritableByteArrayComparable customFilter = new BoundingBoxFilter("-1.,-1., 1.5, 1.5");

		SingleColumnValueFilter singleColumnValueFilterA = new SingleColumnValueFilter(
				famA, coll1, CompareOp.EQUAL, customFilter);
		singleColumnValueFilterA.setFilterIfMissing(true);  

		SingleColumnValueFilter singleColumnValueFilterB = new SingleColumnValueFilter(
				famA, coll2, CompareOp.EQUAL, Bytes.toBytes("hello hbase!"));
		singleColumnValueFilterB.setFilterIfMissing(true);  

		FilterList filter = new FilterList(Operator.MUST_PASS_ALL, Arrays
				.asList((Filter) singleColumnValueFilterA, singleColumnValueFilterB));  

		scan.setFilter(filter);  

		ResultScanner scanner = hTable.getScanner(scan);  

		for (Result result : scanner) {
			System.out.println(Bytes.toString(result.getValue(famA, coll1)) + " , "
					+ Bytes.toString(result.getValue(famA, coll2)));
		}
	}
}