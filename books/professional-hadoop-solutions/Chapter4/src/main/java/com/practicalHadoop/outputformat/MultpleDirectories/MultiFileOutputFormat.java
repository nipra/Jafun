
package com.practicalHadoop.outputformat.MultpleDirectories;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** A base class for {@link OutputFormat}s that read from {@link FileSystem}s.*/
public abstract class MultiFileOutputFormat<K, V> extends FileOutputFormat<K, V> {

  protected static final String PART = "part";
  private FileOutputCommitter committer = null;

  /**
   * Set the base output name for output file to be created.
  */ 
  protected static void setOutputName(JobContext job, String name) {
    job.getConfiguration().set(BASE_OUTPUT_NAME, name);
  }

  public synchronized 
     OutputCommitter getOutputCommitter(TaskAttemptContext context
                                        ) throws IOException {
    if (committer == null) {
      Path output = getOutputPath(context);
      committer = new FileOutputCommitter(output, context);
    }
    return committer;
  }
}

