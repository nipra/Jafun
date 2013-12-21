package com.practicalHadoop.queue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BytesConverter {

	public static Object bytesToObject(byte[] bytes) throws IOException{
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		}
		catch (ClassNotFoundException e){
			e.printStackTrace();
			throw new IOException("bytesToobject conversion failed!", e);
		}
	}

	public static byte[] objectToBytes(Object object) throws IOException{
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
		oos.writeObject(object);
		oos.flush();
		oos.close();
		byteOutputStream.close();
		return byteOutputStream.toByteArray();
	}
}