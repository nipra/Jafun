package com.practicalHadoop.lucene.document;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data structure for a document in a term definition.
 */
public class SpatialTermDocument {
	private int _docFrequency;
	private int[] _docPositions;
	private String _geometryType;
	private Map<Integer, List<Long>> _tierBoxes;
	
	public SpatialTermDocument(int freq, final int[]pos /*, final TermVectorOffsetInfo [] offsets */ ){

		_docFrequency = freq;
		_docPositions = pos;
		_geometryType = null;
		_tierBoxes = null;
	}

	public int getDocFrequency() {
		return _docFrequency;
	}


	public int[] getDocPositions() {
		return _docPositions;
	}

	public String getGeometryType() {
		return _geometryType;
	}

	public void setGeometryType(String geometryType) {
		_geometryType = geometryType;
	}

	public Set<Integer> getTiers(){
		if(_tierBoxes == null)
			return null;
		return _tierBoxes.keySet();
	}
	
	public List<Long> getTierBoxes(int tier) {
		
		if(_tierBoxes == null)
			return null;
		return _tierBoxes.get(tier);
	}

	public void setTierBoxes(Map<Integer, List<Long>> tierBoxes) {
		_tierBoxes = tierBoxes;
	}

	public String toString(){

		StringBuffer rb = new StringBuffer();
		rb.append("\t\t\tdoc Frequency " + _docFrequency);
		if(_docPositions != null){
			rb.append(" positions ");
			boolean first = true;
			for(int p : _docPositions){
				if(first)
					first = false;
				else
					rb.append(";");
				rb.append(p);
			}
		}
		if(_geometryType != null){
			rb.append(" Gemetry shape " + _geometryType);
		}
		if(_tierBoxes != null){
			rb.append(" Tiers ");
			for(int tier : _tierBoxes.keySet()){
				rb.append(" T" + tier + " ");
				boolean first = true;
				for(long box : _tierBoxes.get(tier)){
					if(first)
						first = false;
					else
						rb.append(";");
					rb.append(box);
				}
			}
		} 
		return rb.toString();
	}
}	