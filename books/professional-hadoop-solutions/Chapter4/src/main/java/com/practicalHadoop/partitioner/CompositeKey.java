package com.practicalHadoop.partitioner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class CompositeKey implements WritableComparable<CompositeKey> {
	
	private long productID;
	private long purchaseID;
	
	public CompositeKey(){}

	public CompositeKey(long productID, long purchaseID){
		this.productID = productID;
		this.purchaseID = purchaseID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}

	public void setPurchaseID(long purchaseID) {
		this.purchaseID = purchaseID;
	}

	public long getProductID() {
		return productID;
	}

	public long getPurchaseID() {
		return purchaseID;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		productID = in.readLong();
		purchaseID = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(productID);
		out.writeLong(purchaseID);		
	}

	@Override
	public int compareTo(CompositeKey key) {
		int result = new Long(productID).compareTo(new Long(key.getProductID()));
		if(result != 0)
			return result;
		return new Long(purchaseID).compareTo(new Long(key.getPurchaseID()));
	}

}
