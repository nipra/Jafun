
package com.practicalHadoop.outputformat.MultpleDirectories;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MultiTextOutputFormat<K, V> extends TextOutputFormat<K, V> {

	private FileOutputCommitter committer = null;

	@Override
	public synchronized 
	OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException {
		if (committer == null) {
			Path output = getOutputPath(context);
			committer = new FileOutputCommitter(output, context);
		}
		return committer;
	}
}

