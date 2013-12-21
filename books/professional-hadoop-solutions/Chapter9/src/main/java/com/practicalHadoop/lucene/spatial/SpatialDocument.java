package com.practicalHadoop.lucene.spatial;

import org.apache.lucene.document.Document;

import com.practicalHadoop.lucene.spatial.geometry.GeometricShape;


public class SpatialDocument{
	
	private Document _document;
	private GeometricShape _shape;
	
	public SpatialDocument(){
		
		_document = null;
		_shape = null;
	}

	public SpatialDocument(Document doc){
		
		_document = doc;
		_shape = null;
	}

	public SpatialDocument(GeometricShape shape){
		
		_document = null;
		_shape = shape;
	}

	public SpatialDocument(Document doc, GeometricShape shape){
		
		_document = doc;
		_shape = shape;
	}

	public Document getDocument() {
		return _document;
	}

	public void setDocument(Document document) {
		_document = document;
	}

	public GeometricShape getShape() {
		return _shape;
	}

	public void setShape(GeometricShape shape) {
		_shape = shape;
	}
}