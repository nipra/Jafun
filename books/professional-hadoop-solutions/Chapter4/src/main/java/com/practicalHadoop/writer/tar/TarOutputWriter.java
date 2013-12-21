package com.practicalHadoop.writer.tar;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

class TarOutputWriter extends RecordWriter<BytesWritable, Text> {

    private final TarArchiveOutputStream tarOutStream;

    public TarOutputWriter(Configuration conf, Path output) throws FileNotFoundException,
            IOException {
        FileSystem fs = output.getFileSystem(conf);
        FSDataOutputStream fsOutStream = fs.create(output);
        tarOutStream = new TarArchiveOutputStream(fsOutStream);
    }

    @Override
    public void write(BytesWritable key, Text value) throws IOException {

        if (key == null || value == null ) {
            return;
        }
		TarArchiveEntry mtd = new TarArchiveEntry(key.toString());
		mtd.setSize(value.getLength());
		tarOutStream.putArchiveEntry(mtd);
        IOUtils.copy(new ByteArrayInputStream(value.getBytes()), tarOutStream);
        tarOutStream.closeArchiveEntry();
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException {
        if (tarOutStream != null) {
            tarOutStream.flush();
            tarOutStream.finish();
            tarOutStream.close();
         }
    }
}