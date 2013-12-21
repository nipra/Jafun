package com.practicalHadoop.inputformat.multitable;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Writable;

public class TableDefinition implements Writable {

	/** Holds the details for the internal scanner. */
	private Scan _scan = null;
	/** The table to scan. */
	private HTable _table = null;
	/** The table to scan. */
	private String _tableName = null;

	public TableDefinition(HTable table, Scan scan){
		_table = table;
		_tableName = Bytes.toString(table.getTableName());
		_scan = scan;
		if(_scan == null)
			_scan = new Scan();
	}

	public TableDefinition(String tName, Scan scan){
		_tableName = tName;
		_table = null;
		_scan = scan;
		if(_scan == null)
			_scan = new Scan();
	}

	public TableDefinition(DataInputStream dis) throws IOException{
		readFields(dis);
		_table = null;
	}
	
	public TableDefinition(DataInputStream dis, Configuration conf) throws IOException{
		readFields(dis);
		_table = new HTable(new HBaseConfiguration(conf), _tableName);
	}
	
	public Scan getScan() {
		return _scan;
	}

	public HTable getTable() {
		return _table;
	}

	public String getTableName() {
		return _tableName;
	}

	@Override
	public void readFields(final DataInput in) throws IOException {

		_tableName = in.readUTF();
		_scan = new Scan();
		_scan.readFields(in);
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeUTF(_tableName);
		_scan.write(out);
	}
}
