package com.practicalHadoop.hbase.tables;

import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.regionserver.StoreFile.BloomType;
import org.apache.hadoop.hbase.util.Bytes;

import com.practicalHadoop.hbase.tables.configuration.ColumnFamily;
import com.practicalHadoop.hbase.tables.configuration.TableType;
import com.practicalHadoop.hbase.tables.configuration.TablesType;

public class TableCreator {
	
	
//	public static void main(String[] args) throws Exception {

//		Configuration conf = ConfigManager.getCofiguration("SandBox_Cloud_Config.xml");
//		List<HTable> tables = getTables("HBASETables.xml", conf);
//	}
	
	public static List<HTable> getTables(TablesType tables, Configuration conf)throws Exception{


		HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
		List<HTable> result = new LinkedList<HTable>(); 

		for (TableType table : tables.getTable()) {
			HTableDescriptor desc = null;
			if (hBaseAdmin.tableExists(table.getName())) {
				if (tables.isRebuild()) {
					hBaseAdmin.disableTable(table.getName());
					hBaseAdmin.deleteTable(table.getName());
					createTable(hBaseAdmin, table);
				}
				else{
					byte[] tBytes = Bytes.toBytes(table.getName());
					desc = hBaseAdmin.getTableDescriptor(tBytes);
					List<ColumnFamily> columns = table.getColumnFamily();
			        for(ColumnFamily family : columns){
			        	boolean exists = false;
			        	String name = family.getName();
			        	for(HColumnDescriptor fm : desc.getFamilies()){
			        		String fmName = Bytes.toString(fm.getName());
			        		if(name.equals(fmName)){
			        			exists = true;
			        			break;
			        		}
			        	}
			        	if(!exists){
			        		System.out.println("Adding Famoly " + name + " to the table " + table.getName());
			        		hBaseAdmin.addColumn(tBytes, buildDescriptor(family));
			        	}
			        }
				}
			} else {
				createTable(hBaseAdmin, table);
			}
			result.add( new HTable(conf, Bytes.toBytes(table.getName())) );
		}
		return result;
	}
	
	private static void createTable(HBaseAdmin hBaseAdmin,TableType table) throws Exception{
		HTableDescriptor desc = new HTableDescriptor(table.getName());
		if(table.getMaxFileSize() != null){
			Long fs = 1024l * 1024l *  table.getMaxFileSize();
			desc.setValue(HTableDescriptor.MAX_FILESIZE, fs.toString());
		}
		List<ColumnFamily> columns = table.getColumnFamily();
        for(ColumnFamily family : columns){
        	desc.addFamily(buildDescriptor(family));
        }
        hBaseAdmin.createTable(desc);
	}
	
	private static HColumnDescriptor buildDescriptor(ColumnFamily family){

    	HColumnDescriptor col = new HColumnDescriptor(family.getName());
    	if(family.isBlockCacheEnabled() != null)	        	
    		col.setBlockCacheEnabled(family.isBlockCacheEnabled());
    	if(family.isInMemory() != null)	        	
    		col.setInMemory(family.isInMemory());
    	if(family.isBloomFilter() != null)	        	
    		col.setBloomFilterType(BloomType.ROWCOL);
    	if(family.getMaxBlockSize() != null){
    		int bs = 1024 * 1024 * family.getMaxBlockSize();
    		col.setBlocksize(bs);
    	}
    	if(family.getMaxVersions() != null)	        	
    		col.setMaxVersions(family.getMaxVersions().intValue());
    	if(family.getTimeToLive() != null)	        	
    		col.setTimeToLive(family.getTimeToLive());
    	return col;
	}
}