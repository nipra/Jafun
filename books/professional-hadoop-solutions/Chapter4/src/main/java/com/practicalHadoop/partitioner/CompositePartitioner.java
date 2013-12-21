package com.practicalHadoop.partitioner;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class CompositePartitioner extends Partitioner<CompositeKey, ByteWritable> {

	@Override
	public int getPartition(CompositeKey key, ByteWritable value,int numPartitions) {
		// just partition by the productId since that's
		// how we are grouping for the reducer
		return (int)key.getProductID()% numPartitions;
	}
}
