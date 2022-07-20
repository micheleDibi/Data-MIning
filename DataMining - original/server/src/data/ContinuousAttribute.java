package data;

import java.io.Serializable;

/**
 * Gestisce gli attributi continui
 */
public class ContinuousAttribute extends Attribute implements Serializable {

	/**
	 * valore minimo della colonna di attributi continui
	 */
	private double min = Double.POSITIVE_INFINITY;

	/**
	 * valore massimo della colonna di attributi continui
	 */
	private double max = Double.NEGATIVE_INFINITY;

	void setMin(Double v) {
		if (v < min) {
			min = v;
		}
	}

	void setMax(Double v) {
		if (v > max) {
			max = v;
		}
	}

	double scale(Double value) {
		if(min == max)
			return (value - min) / max;

		return ((value - min) / (max - min));
	}

	/**
	 * costruttore della classe
	 * 
	 * @param nm  nome dell'attributo continuo
	 * @param idx indice dell'attributo
	 */
	public ContinuousAttribute(String nm, int idx) {
		super(nm, idx);
	}

	/**
	 * Restituisce la stringa contenente nome e valore dell'attributo continuo, compreso di valore minimo e massimo
	 *
	 * @return stringa con nome, valore, minimo e massimo della colonna
	 */
	public String toString() {
		return super.toString() + " min: " + min + " max: " + max;
	}

}
