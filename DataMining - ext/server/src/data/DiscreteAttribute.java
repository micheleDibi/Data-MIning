package data;

import java.io.Serializable;

/**
 * Gestisce gli attributi discreti
 */
class DiscreteAttribute extends Attribute implements Serializable {

	/**
	 * costruttore della classe
	 * 
	 * @param nm  nome dell'attributo discreto
	 * @param idx indice dell'attributo
	 */
	DiscreteAttribute(String nm, int idx) {
		super(nm, idx);
	}

}
