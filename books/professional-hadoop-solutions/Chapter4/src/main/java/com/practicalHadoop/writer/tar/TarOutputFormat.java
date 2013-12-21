package com.practicalHadoop.writer.tar;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TarOutputFormat extends FileOutputFormat<BytesWritable, Text> {

    //private static final Log log = LogFactory.getLog(TargzOutputFormat.class);
    public static final String EXTENSION = ".tar";

    // output directory is set in superclass
    //can be retrieved using 	getOutputPath(JobConf conf) 

    @Override
    public RecordWriter<BytesWritable, Text> getRecordWriter(TaskAttemptContext job)
            throws IOException, InterruptedException {

        Path outpath = getDefaultWorkFile(job, EXTENSION);
        return new TarOutputWriter(job.getConfiguration(), outpath);
    }
}
