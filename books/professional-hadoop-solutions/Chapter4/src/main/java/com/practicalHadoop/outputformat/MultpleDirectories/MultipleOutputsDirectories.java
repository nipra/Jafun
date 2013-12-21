package com.practicalHadoop.outputformat.MultpleDirectories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.ReflectionUtils;


public class MultipleOutputsDirectories<KEYOUT, VALUEOUT> extends MultipleOutputs<KEYOUT, VALUEOUT> {

	private static final String MO_PREFIX = 
		"mapreduce.multipleoutputs.namedOutput.";
	private static final String FORMAT = ".format";
	private static final String KEY = ".key";
	private static final String VALUE = ".value";
	private static final String MULTIPLE_OUTPUTS = "mapreduce.multipleoutputs";

	/**
	 * Cache for the taskContexts
	 */
	private Map<String, TaskAttemptContext> taskContexts = new HashMap<String, TaskAttemptContext>();
	private Set<String> namedOutputs;
	private TaskInputOutputContext<?, ?, KEYOUT, VALUEOUT> context;
	private Map<String, RecordWriter<?, ?>> recordWriters;

	public MultipleOutputsDirectories(
			TaskInputOutputContext<?, ?, KEYOUT, VALUEOUT> context) {
		super(context);
		this.context = context;
	    namedOutputs = Collections.unmodifiableSet(
	    	      new HashSet<String>(getNamedOutputsList(context)));
	    recordWriters = new HashMap<String, RecordWriter<?, ?>>();
	}

	  /**
	   * Write key and value to baseOutputPath using the namedOutput.
	   * 
	   * @param namedOutput    the named output name
	   * @param key            the key
	   * @param value          the value
	   * @param baseOutputPath base-output path to write the record to.
	   * Note: Framework will generate unique filename for the baseOutputPath
	   */
	@Override  
	@SuppressWarnings("unchecked")
	public <K, V> void write(String namedOutput, K key, V value,
			String baseOutputPath) throws IOException, InterruptedException {
		checkNamedOutputName(context, namedOutput, false);
		checkBaseOutputPath(baseOutputPath);
		if (!namedOutputs.contains(namedOutput)) {
			throw new IllegalArgumentException("Undefined named output '" +
					namedOutput + "'");
		}
		TaskAttemptContext taskContext = getContext(namedOutput);
		getRecordWriter(taskContext, baseOutputPath).write(key, value);
	}

	// Create a taskAttemptContext for the named output with 
	// output format and output key/value types put in the context
	private TaskAttemptContext getContext(String nameOutput) throws IOException {

		TaskAttemptContext taskContext = taskContexts.get(nameOutput);

		if (taskContext != null) {
			return taskContext;
		}

		// The following trick leverages the instantiation of a record writer via
		// the job thus supporting arbitrary output formats.
		Job job = new Job(context.getConfiguration());
		job.setOutputFormatClass(getNamedOutputFormatClass(context, nameOutput));
		job.setOutputKeyClass(getNamedOutputKeyClass(context, nameOutput));
		job.setOutputValueClass(getNamedOutputValueClass(context, nameOutput));
		String location = MultiFileOutputFormat.getOutputPath(job).toString();
		Path jobOutputPath = new Path(location + "/" + nameOutput);
		MultiFileOutputFormat.setOutputPath(job, jobOutputPath);
		taskContext = new TaskAttemptContext(
				job.getConfiguration(), context.getTaskAttemptID());

		taskContexts.put(nameOutput, taskContext);

		return taskContext;
	}

	// by being synchronized MultipleOutputTask can be use with a
	// MultithreadedMapper.
	@SuppressWarnings("unchecked")
	private synchronized RecordWriter getRecordWriter(
			TaskAttemptContext taskContext, String baseFileName) 
					throws IOException, InterruptedException {

		// look for record-writer in the cache
		RecordWriter writer = recordWriters.get(baseFileName);

		// If not in cache, create a new one
		if (writer == null) {
			// get the record writer from context output format
			MultiFileOutputFormat.setOutputName(taskContext, baseFileName);
			try {
				writer = ((OutputFormat) ReflectionUtils.newInstance(
						taskContext.getOutputFormatClass(), taskContext.getConfiguration()))
						.getRecordWriter(taskContext);
			} catch (ClassNotFoundException e) {
				throw new IOException(e);
			}

			// add the record-writer to the cache
			recordWriters.put(baseFileName, writer);
		}
		return writer;
	}
	
	  /**
	   * Closes all the opened outputs.
	   * 
	   * This should be called from cleanup method of map/reduce task.
	   * If overridden subclasses must invoke <code>super.close()</code> at the
	   * end of their <code>close()</code>
	   * 
	   */
	  @SuppressWarnings("unchecked")
	  public void close() throws IOException, InterruptedException {
	    for (RecordWriter writer : recordWriters.values()) {
	      writer.close(context);
	    }
	  }	
	  /**
	 * Checks if output name is valid.
	 *
	 * name cannot be the name used for the default output
	 * @param outputPath base output Name
	 * @throws IllegalArgumentException if the output name is not valid.
	 */
	private static void checkBaseOutputPath(String outputPath) {
		if (outputPath.equals(MultiFileOutputFormat.PART)) {
			throw new IllegalArgumentException("output name cannot be 'part'");
		}
	}

	/**
	 * Checks if a named output name is valid.
	 *
	 * @param namedOutput named output Name
	 * @throws IllegalArgumentException if the output name is not valid.
	 */
	private static void checkNamedOutputName(JobContext job,
			String namedOutput, boolean alreadyDefined) {
		checkTokenName(namedOutput);
		checkBaseOutputPath(namedOutput);
		List<String> definedChannels = getNamedOutputsList(job);
		if (alreadyDefined && definedChannels.contains(namedOutput)) {
			throw new IllegalArgumentException("Named output '" + namedOutput +
			"' already alreadyDefined");
		} else if (!alreadyDefined && !definedChannels.contains(namedOutput)) {
			throw new IllegalArgumentException("Named output '" + namedOutput +
			"' not defined");
		}
	}

	/**
	 * Checks if a named output name is valid token.
	 *
	 * @param namedOutput named output Name
	 * @throws IllegalArgumentException if the output name is not valid.
	 */
	private static void checkTokenName(String namedOutput) {
		if (namedOutput == null || namedOutput.length() == 0) {
			throw new IllegalArgumentException(
					"Name cannot be NULL or emtpy");
		}
		for (char ch : namedOutput.toCharArray()) {
			if ((ch >= 'A') && (ch <= 'Z')) {
				continue;
			}
			if ((ch >= 'a') && (ch <= 'z')) {
				continue;
			}
			if ((ch >= '0') && (ch <= '9')) {
				continue;
			}
			throw new IllegalArgumentException(
					"Name cannot be have a '" + ch + "' char");
		}
	}

	  // Returns list of channel names.
	private static List<String> getNamedOutputsList(JobContext job) {
		List<String> names = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(
				job.getConfiguration().get(MULTIPLE_OUTPUTS, ""), " ");
		while (st.hasMoreTokens()) {
			names.add(st.nextToken());
		}
		return names;
	}

	// Returns the named output OutputFormat.
	@SuppressWarnings("unchecked")
	private static Class<? extends OutputFormat<?, ?>> getNamedOutputFormatClass(
			JobContext job, String namedOutput) {
		return (Class<? extends OutputFormat<?, ?>>)
		job.getConfiguration().getClass(MO_PREFIX + namedOutput + FORMAT, null,
				OutputFormat.class);
	}

	// Returns the key class for a named output.
	private static Class<?> getNamedOutputKeyClass(JobContext job,
			String namedOutput) {
		return job.getConfiguration().getClass(MO_PREFIX + namedOutput + KEY, null,
				WritableComparable.class);
	}

	// Returns the value class for a named output.
	private static Class<? extends Writable> getNamedOutputValueClass(JobContext job, String namedOutput) {
		return job.getConfiguration().getClass(MO_PREFIX + namedOutput + VALUE,
				null, Writable.class);
	}
}