package com.practicalHadoop.inputformat.multitable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.hbase.util.Bytes;

public class MultiTableSplit extends TableSplit {

	private Scan scan;

	/** Default constructor. */
	public MultiTableSplit() {
		super();
		scan = new Scan();
	}

	/**
	 * Creates a new instance while assigning all variables.
	 * 
	 * @param tableName  The name of the current table.
	 * @param startRow  The start row of the split.
	 * @param endRow  The end row of the split.
	 * @param location  The location of the region.
	 */
	public MultiTableSplit(byte [] tableName, byte [] startRow, byte [] endRow,
			final String location, Scan scan) {
		super(tableName, startRow, endRow, location);
		this.scan = scan;
		if(this.scan == null)
			this.scan = new Scan();
	}

	/** 
	 * Returns the scan.
	 * 
	 * @return The Scan. 
	 */
	public Scan getScan() {
		return scan;
	}


	@Override
	public int compareTo(TableSplit split) {

		int comp = Bytes.compareTo(getTableName(), split.getTableName());
		if(comp != 0)
			return comp;
		return Bytes.compareTo(getStartRow(), split.getStartRow());
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		super.readFields(in);
		scan = new Scan();
		scan.readFields(in);
	}

	/**
	 * Writes the field values to the output.
	 * 
	 * @param out  The output to write to.
	 * @throws IOException When writing the values to the output fails.
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		scan.write(out);
	}
}
