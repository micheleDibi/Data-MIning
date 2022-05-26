package data;

import java.io.Serializable;

public class ContinuousAttribute extends Attribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
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
		return ((value - min) / (max - min));
	}

	public ContinuousAttribute(String nm, int idx) {
		super(nm, idx);
	}
	
	public String toString() {
		return super.toString() + " min: " + min + " max: " + max;
	}

}
