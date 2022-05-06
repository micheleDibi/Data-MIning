package data;
abstract class Attribute {

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
	
}
