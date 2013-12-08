// A program to compress data read from standard input and write to standard output
package com.nipra.htdg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ echo "Text" | java -cp target/mr-0.0.1-SNAPSHOT.jar com.nipra.htdg.StreamCompressor org.apache.hadoop.io.compress.GzipCodec | gunzip -
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ echo "Text" | java -cp target/classes:target/lib/* com.nipra.htdg.StreamCompressor org.apache.hadoop.io.compress.GzipCodec | gunzip -

public class StreamCompressor {

  public static void main(String[] args) throws Exception {
    String codecClassname = args[0];
    Class<?> codecClass = Class.forName(codecClassname);
    Configuration conf = new Configuration();
    CompressionCodec codec = (CompressionCodec)
      ReflectionUtils.newInstance(codecClass, conf);

    CompressionOutputStream out = codec.createOutputStream(System.out);
    IOUtils.copyBytes(System.in, out, 4096, false);
    out.finish();
  }
  
}
