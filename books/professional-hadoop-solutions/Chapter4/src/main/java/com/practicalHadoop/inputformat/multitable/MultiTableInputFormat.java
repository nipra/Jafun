package com.practicalHadoop.inputformat.multitable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableRecordReader;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.StringUtils;

public class MultiTableInputFormat extends InputFormat<ImmutableBytesWritable, Result> implements Configurable{

	private final Log LOG = LogFactory.getLog(MultiTableInputFormat.class);

	/** Job parameter that specifies the input tables definitions. */
	public static final String INPUT_TABLES = "hbase.mapreduce.inputtabledefrinitions";

	/** The configuration. */
	private Configuration conf = null;
	/** Tables **/
	private List<TableDefinition> tables;
	/** The reader scanning the table, can be a custom one. */
	private TableRecordReader tableRecordReader = null;

	/**
	 * Allows subclasses to set the {@link TableRecordReader}.
	 *
	 * @param tableRecordReader A different {@link TableRecordReader} 
	 *   implementation.
	 */
	protected void setTableRecordReader(TableRecordReader tableRecordReader) {
		this.tableRecordReader = tableRecordReader;
	}

	/**
	 * Use this before submitting a TableMap job. It will appropriately set up 
	 * the job.
	 * 
	 * @param tables  List of the tables definitions (with scans)
	 * @param mapper  The mapper class to use.
	 * @param outputKeyClass  The class of the output key.
	 * @param outputValueClass  The class of the output value.
	 * @param job  The current job to adjust.
	 * @throws IOException When setting up the details fails.
	 */
	public static void initTableMapperJob(List<TableDefinition> tables,
			Class<? extends TableMapper> mapper, 
			Class<? extends WritableComparable> outputKeyClass, 
			Class<? extends Writable> outputValueClass, Job job) throws IOException {
		job.setInputFormatClass(MultiTableInputFormat.class);
		if (outputValueClass != null) job.setMapOutputValueClass(outputValueClass);
		if (outputKeyClass != null) job.setMapOutputKeyClass(outputKeyClass);
		job.setMapperClass(mapper);
		job.getConfiguration().set(INPUT_TABLES,convertTablesToString(tables));
	}

	/**
	 * Writes the given TableDefinitions List into a Base64 encoded string.
	 * 
	 * @param scan  The scan to write out.
	 * @return The scan saved in a Base64 encoded string.
	 * @throws IOException When writing the scan fails.
	 */
	static String convertTablesToString(List<TableDefinition> tables) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeInt(tables.size());
		for(TableDefinition def : tables)
			def.write(dos);
		return Base64.encodeBytes(out.toByteArray());
	}

	/**
	 * Converts the given Base64 string back into a list of table definitions.
	 * 
	 * @param base64  The scan details.
	 * @return The newly created Scan instance.
	 * @throws IOException When reading the scan instance fails.
	 */
	static List<TableDefinition> convertStringToTables(String base64, Configuration conf) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(base64));
		DataInputStream dis = new DataInputStream(bis);
		int nTables = dis.readInt();
		List<TableDefinition> tables = new ArrayList<TableDefinition>(nTables);
		for(int i = 0; i < nTables; i++)
			tables.add(new TableDefinition(dis, conf));
		return tables;
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.conf = configuration;
		String tableNames = conf.get(INPUT_TABLES);
		try {
			tables = convertStringToTables(tableNames, conf);
		} catch (Exception e) {
			LOG.error(StringUtils.stringifyException(e));
		}
	}

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException,
	InterruptedException {

		List<InputSplit> splits = new LinkedList<InputSplit>(); 
		int count = 0;
		for(TableDefinition t : tables){
			HTable table = t.getTable();
			Scan scan = t.getScan();
			if (table == null) {
				continue;
			}
			Pair<byte[][], byte[][]> keys = table.getStartEndKeys();
			if (keys == null || keys.getFirst() == null || 
					keys.getFirst().length == 0) {
				continue;
			}
			for (int i = 0; i < keys.getFirst().length; i++) {
				String regionLocation = table.getRegionLocation(keys.getFirst()[i]).
				getServerAddress().getHostname();
				byte[] startRow = scan.getStartRow();
				byte[] stopRow = scan.getStopRow();
				// determine if the given start an stop key fall into the region
				if ((startRow.length == 0 || keys.getSecond()[i].length == 0 ||
						Bytes.compareTo(startRow, keys.getSecond()[i]) < 0) &&
						(stopRow.length == 0 || 
								Bytes.compareTo(stopRow, keys.getFirst()[i]) > 0)) {
					byte[] splitStart = startRow.length == 0 || 
					Bytes.compareTo(keys.getFirst()[i], startRow) >= 0 ? 
							keys.getFirst()[i] : startRow;
							byte[] splitStop = (stopRow.length == 0 || 
									Bytes.compareTo(keys.getSecond()[i], stopRow) <= 0) &&
									keys.getSecond()[i].length > 0 ? 
											keys.getSecond()[i] : stopRow;
											InputSplit split = new MultiTableSplit(table.getTableName(),
													splitStart, splitStop, regionLocation, scan);
											splits.add(split);
											if (LOG.isDebugEnabled()) 
												LOG.debug("getSplits: split -> " + (count++) + " -> " + split);
				}
			}
		}
		if(splits.size() == 0){
			throw new IOException("Expecting at least one region.");
		}
		return splits;
	}

	@Override
	public RecordReader<ImmutableBytesWritable, Result> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		MultiTableSplit tSplit = (MultiTableSplit) split;
		TableRecordReader trr = this.tableRecordReader;
		// if no table record reader was provided use default
		if (trr == null) {
			trr = new TableRecordReader();
		}
		Scan sc = tSplit.getScan();
		sc.setStartRow(tSplit.getStartRow());
		sc.setStopRow(tSplit.getEndRow());
		trr.setScan(sc);
		byte[] tName = tSplit.getTableName();
		trr.setHTable(new HTable(HBaseConfiguration.create(conf), tName));
		trr.init();
		return trr;
	}
}
