package com.nipra.mr.cascading.impatient;

import java.util.Properties;

import cascading.property.AppProps;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.scheme.hadoop.TextDelimited;
import cascading.pipe.Pipe;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;

// hadoop fs -rmr /data/rain.txt
// hadoop fs -rmr /output/rain
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ hadoop fs -put ~/Softwares/Hadoop/Cascading/Impatient/part1/data/rain.txt /data
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ mvn clean compile install
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ hadoop jar target/mr-0.0.1-SNAPSHOT.jar com.nipra.mr.cascading.impatient.Copy /data/rain.txt /output/rain
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ hadoop fs -cat /output/rain/part-00000
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ hadoop fs -cat /output/rain/part-00001

public class Copy {
  public static void main(String[] args) {
    String inPath = args[0];
    String outPath = args[1];

    Properties properties = new Properties();
    AppProps.setApplicationJarClass(properties, Copy.class);
    
    HadoopFlowConnector flowConnector = new HadoopFlowConnector(properties);

    TextDelimited textDelimited =  new TextDelimited(true, "\t");
    Tap inTap = new Hfs(textDelimited, inPath);
    Tap outTap = new Hfs(textDelimited, outPath);

    Pipe copyPipe = new Pipe("copy");

    FlowDef flowDef = FlowDef.flowDef()
      .addSource(copyPipe, inTap)
      .addTailSink(copyPipe, outTap);

    flowConnector.connect(flowDef).complete();
  }
}
