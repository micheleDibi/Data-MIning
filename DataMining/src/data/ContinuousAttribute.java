package data;
public class ContinuousAttribute extends Attribute {
	
	private double min = Double.POSITIVE_INFINITY;
	private double max = Double.NEGATIVE_INFINITY;
	
	void setMin(Double v) {
		if(v < min) {
			min = v;
		}
	}
	
	void setMax(Double v) {
		if(v > max) {
			max = v;
		}
	}
	
	double scale(Double value) {
		//System.out.println(this.toString());
		return ((value - min) / (max - min));
	}

	public ContinuousAttribute(String nm, int idx) {
		super(nm, idx);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		return super.toString() + "; min: " + min + "; max: " + max;
	}

}
