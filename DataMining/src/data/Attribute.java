package data;

import java.io.Serializable;

abstract class Attribute implements Serializable{

	private final String name;
	private int index;
	
	Attribute(String nm, int idx) {
		name = nm;
		index = idx;
	}
	
	String getName() {
		return name;
	}
	
	int getIndex() {
		return index;
	}
	
	public String toString() {
		return "["+index+"]" + name;
	}
	
}
