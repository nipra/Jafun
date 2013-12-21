package com.practicalHadoop.queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This is a simple HBase-based queue. Currently supported is just enqueuing and dequeuing messages
 * (single/group), where a message is just a message body. A simple change can be done to implement
 * support for a message metadata. A Queue is implemented as an HBase table with a special row key
 * for the message pointer and a row per message. Known Limitation: Message sequence is not
 * guaranteed
 */

public class HdQueue {

	private static final Logger LOG = LoggerFactory.getLogger(HdQueue.class);
	public static final byte[] MESSAGE_FAMILY = "message".getBytes();
	public static final byte[] MESSAGE_COLUMN = "value".getBytes();
	public static final byte[] COUNTER_COLUMN = "counter".getBytes();
	public static final byte[] COUNTER_KEY = "counterKey".getBytes();
		
	private HTable table = null;
	private String qName;
	
	/**
	 * Get the existing HBase Queue of the given name. Note that each Queue is uniquely identified
	 * by its name.
	 * 
	 * @param qName: Name of the Queue
	 * @return: the existing HBase Queue with the given name.
	 * @throws Exception
	 */
	public static HdQueue getQueue(String qName) throws IOException{
		Configuration conf = new Configuration();
		return new HdQueue(qName, conf, false);
	}
	
	/**
	 * Create a new HBase Queue with the given name.
	 * 
	 * @param qName: New of the new HBase Queue
	 * @return: Newly created HBase Queue of the given name.
	 * @throws IOException
	 */
	public static HdQueue createQueue(String qName) throws IOException{
		Configuration conf = new Configuration();
		return new HdQueue(qName, conf, true);
	}
	
	
	// Constructor - connecting to the queue
	public HdQueue(String tName, String cFile, boolean create) throws IOException{
		this(tName, getConf(cFile), create);
	}
	
	/**
	 * 
	 * @param tName
	 * @param conf
	 * @param create
	 * @throws IOException
	 */
	public HdQueue(String tName, Configuration conf, boolean create) throws IOException{
		this.qName = tName;
		if (create)
			createQueueTable(tName, conf);
		else
			table = new HTable(conf, tName.getBytes());
	}
	
	private static Configuration getConf(String cFile){
		if (cFile != null)
			Configuration.addDefaultResource(cFile);
		return new Configuration();
	}
	
	// Check if the table exists and create a new one otherwise
	private void createQueueTable(String tName, Configuration conf) throws IOException{
		HBaseAdmin admin = new HBaseAdmin(conf);
		boolean exists = admin.tableExists(tName);
		
		if (!exists){
			LOG.info("start to create table " + tName);
			// Create table
			HTableDescriptor desc = new HTableDescriptor(tName);
			HColumnDescriptor family = new HColumnDescriptor(MESSAGE_FAMILY);
			family.setInMemory(true);
			desc.addFamily(family);
			admin.createTable(desc);
			LOG.info(tName + " table created");
		}
		table = new HTable(conf, tName.getBytes());
		if (!exists){
			// Set queue pointer
			Put put = new Put(COUNTER_KEY);
			put.add(MESSAGE_FAMILY, COUNTER_COLUMN, Bytes.toBytes(0L));
			table.put(put);
		}
	}
	
	// Enqueue the message
	public void enqueue(byte[] data) throws IOException{
		
		long messageKey = 0;
		// Make sure that the pointer is >= 0
		while ((messageKey = table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, 1)) < 0){};
		// Put the message
		String sKey = Long.toString(messageKey); // Dead store .. violation (Sonar), bug in Sonar
		Put put = new Put(Bytes.toBytes(sKey));
		put.add(MESSAGE_FAMILY, MESSAGE_COLUMN, data);
		table.put(put);
	}
	
	public long getCurrentQueueSize() throws IOException{
		return table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, 0);
	}
	
	public void enqueueString(String msg) throws IOException{
		byte[] data = Bytes.toBytes(msg);
		enqueue(data);
	}
	
	public void enqueueString(List<String> msgList) throws IOException{
		if (msgList.isEmpty()){
			return;
		}
		List<byte[]> data = new ArrayList<byte[]>();
		for (String msg : msgList){
			data.add(Bytes.toBytes(msg));
		}
		enqueue(data);
	}
	
	public void enqueueObject(Object obj) throws IOException{
		enqueue(BytesConverter.objectToBytes(obj));
	}
	
	public void enqueueObjectList(List<Object> olist) throws IOException{
		List<byte[]> byteList = new ArrayList<byte[]>();
		for (Object obj : olist){
			byteList.add(BytesConverter.objectToBytes(obj));
		}
		enqueue(byteList);
	}
	
	// Enqueue multiple messages
	public void enqueue(List<byte[]> data) throws IOException{
		int items = data.size();
		long lastKey = table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, items);
		int valCount = 0;
		long firstKey = lastKey + 1 - items;
		List<Put> puts = new ArrayList<Put>(items);
		for (; lastKey >= 0; lastKey--, valCount++){
			String sKey = Long.toString(lastKey);
			Put put = new Put(Bytes.toBytes(sKey));
			put.add(MESSAGE_FAMILY, MESSAGE_COLUMN, data.get(valCount));
			data.remove(valCount);
			puts.add(put);
		}
		table.put(puts);
		if (valCount < items)
			enqueue(data);
	}
	
	// Dequeue the message
	public byte[] dequeue() throws IOException{
		
		long messageKey = table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, -1);
		if (messageKey < 0){
			// Restore the pointer
			messageKey = table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, 1);
			return null;
		}
		String sKey = Long.toString(++messageKey);
		Get get = new Get(Bytes.toBytes(sKey));
		get.addColumn(MESSAGE_FAMILY, MESSAGE_COLUMN);
		Result result = table.get(get);
		return result.value();
	}
	
	public List<String> dequeueString(int nm) throws IOException{
		List<String> rList = new ArrayList<String>();
		List<byte[]> bMsgList = dequeue(nm);
		for (byte[] bMsg : bMsgList){
			rList.add(Bytes.toString(bMsg));
		}
		return rList;
	}
	
	public List<Object> dequeueObject(int nm) throws IOException{
		List<byte[]> byteList = dequeue(nm);
		List<Object> olist = new ArrayList<Object>();
		for (byte[] bt : byteList){
			olist.add(BytesConverter.bytesToObject(bt));
		}
		return olist;
	}
	
	public Object dequeueObject() throws IOException{
		byte[] bt = dequeue();
		return BytesConverter.bytesToObject(bt);
	}
	
	// Dequeue multiple messages
	public List<byte[]> dequeue(int nm) throws IOException{
		long messageKey = table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, -nm);
		List<Get> gets = new ArrayList<Get>(nm);
		for(; (messageKey > 0) && (nm > 0); messageKey--, nm--){
			String sKey = Long.toString(messageKey);
			Get get = new Get(Bytes.toBytes(sKey));
			get.addColumn(MESSAGE_FAMILY, MESSAGE_COLUMN);
			gets.add(get);
		}
		List<byte[]> result = new ArrayList<byte[]>(nm);
		if (gets.size() > 0){
			Result[] reads = table.get(gets);
			for (Result read : reads){
				result.add(read.value());
			}
		}
		if(nm > 0){
			result.addAll(dequeue(nm));
		}
		return result;
	}
	
	public void close() throws IOException{
		table.close();
	}
	
	/**
	 * Destroy the given queue if it exists.
	 * 
	 * @param qName: name of the queue used to create internal HBase table.
	 * @throws HdTableCheckException
	 */
	public static void destroy(String qName) throws Exception{
		Configuration conf = new Configuration();
		HBaseAdmin admin;

		admin = new HBaseAdmin(conf);
		boolean exists = admin.tableExists(qName);
		if (exists){
			admin.disableTable(qName);
			admin.deleteTable(qName);
		}
	}
	
	/**
	 * Used this constructor for unit testing only
	 * 
	 * @param tName
	 * @param cFile
	 */
	public HdQueue(String tName, HTable table){
		qName = tName;
		this.table = table;
	}
	
	public void destroy() throws IOException{
		Configuration conf = new Configuration();
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(table.getTableName());
		admin.deleteTable(table.getTableName());
	}
	
	public String getQname(){
		return qName;
	}
	
	/**
	 * 
	 * @param hbReturnedKey: HBase returned key
	 * @param nmRequest: Number of keys requested to HBase table.
	 * @return List of 2 values: first valid key, and the number of keys.
	 * @throws IOException
	 */
	protected List<Long> getValidKeys(long hbReturnedKey, int nmRequest) throws IOException{
		List<Long> rVal = new ArrayList<Long>();
		if (hbReturnedKey >= 0)
		{
			// NOTE: valid key starts from 1
			rVal.add(++hbReturnedKey);
			rVal.add((long) nmRequest);
			return rVal;
		}
		
		long valCount = nmRequest + hbReturnedKey;
		if (valCount <= 0)
		{
			table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, nmRequest);
			rVal.add((long) 0);
			rVal.add((long) 0);
			return rVal;
		}
		
		table.incrementColumnValue(COUNTER_KEY, MESSAGE_FAMILY, COUNTER_COLUMN, -hbReturnedKey);
		rVal.add((long) 1);
		rVal.add(valCount);
		return rVal;
	}
}
