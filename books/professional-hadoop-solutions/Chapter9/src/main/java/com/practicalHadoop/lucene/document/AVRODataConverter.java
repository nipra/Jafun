package com.practicalHadoop.lucene.document;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class AVRODataConverter {
	
	private static EncoderFactory eFactory = new EncoderFactory();
	private static DecoderFactory dFactory = new DecoderFactory();

	private static SpecificDatumWriter<singleField> fwriter = new SpecificDatumWriter<singleField>(singleField.class);
	private static SpecificDatumReader<singleField> freader = new SpecificDatumReader<singleField>(singleField.class);
	private static SpecificDatumWriter<FieldsData> fdwriter = new SpecificDatumWriter<FieldsData>(FieldsData.class);
	private static SpecificDatumReader<FieldsData> fdreader = new SpecificDatumReader<FieldsData>(FieldsData.class);
	private static SpecificDatumWriter<TermDocument> twriter = new SpecificDatumWriter<TermDocument>(TermDocument.class);
	private static SpecificDatumReader<TermDocument> treader = new SpecificDatumReader<TermDocument>(TermDocument.class);
	private static SpecificDatumWriter<TermDocumentFrequency> dwriter = new SpecificDatumWriter<TermDocumentFrequency>(TermDocumentFrequency.class);
	private static SpecificDatumReader<TermDocumentFrequency> dreader = new SpecificDatumReader<TermDocumentFrequency>(TermDocumentFrequency.class);

	private AVRODataConverter(){}
	
	public static byte[] toBytes(singleField fData)throws Exception{
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Encoder encoder = eFactory.binaryEncoder(outputStream, null);		
		fwriter.write(fData, encoder);		
		encoder.flush();
		return outputStream.toByteArray();
	}
	
	public static byte[] toBytes(FieldsData fData)throws Exception{
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Encoder encoder = eFactory.binaryEncoder(outputStream, null);		
		fdwriter.write(fData, encoder);		
		encoder.flush();
		return outputStream.toByteArray();
	}
	
	public static byte[] toBytes(TermDocument td)throws Exception{
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Encoder encoder = eFactory.binaryEncoder(outputStream, null);		
		twriter.write(td, encoder);		
		encoder.flush();
		return outputStream.toByteArray();
	}
	
	public static byte[] toBytes(TermDocumentFrequency tdf)throws Exception{
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Encoder encoder = eFactory.binaryEncoder(outputStream, null);		
		dwriter.write(tdf, encoder);		
		encoder.flush();
		return outputStream.toByteArray();
	}
		
	public static singleField unmarshallSingleData(byte[] inputStream)throws Exception{
		
		Decoder decoder = dFactory.binaryDecoder(inputStream, null);
		return freader.read(null, decoder);
	}
	
	public static FieldsData unmarshallFieldData(byte[] inputStream)throws Exception{
		
		Decoder decoder = dFactory.binaryDecoder(inputStream, null);
		return fdreader.read(null, decoder);
	}
	
	public static TermDocument unmarshallTermDocument(byte[] inputStream)throws Exception{
		
		Decoder decoder = dFactory.binaryDecoder(inputStream, null);
		return treader.read(null, decoder);
	}
	
	public static TermDocumentFrequency unmarshallTermDocumentFrequency(byte[] inputStream)throws Exception{
		
		Decoder decoder = dFactory.binaryDecoder(inputStream, null);
		return dreader.read(null, decoder);
	}
}