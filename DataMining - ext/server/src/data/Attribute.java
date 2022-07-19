package data;

import java.io.Serializable;

abstract class Attribute implements Serializable {

	private final String name;
	private final int index;

	Attribute(String nm, int idx) {
		name = nm;
		index = idx;
	}

	/**
	 * restituisce il nome dell'attributo
	 * 
	 * @return restituisce il nome dell'attributo
	 */
	public String getName() {
		return name;
	}

	/**
	 * restituisce l'indice dell'attributo
	 * 
	 * @return restituisce l'indice dell'attributo
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * restituisce la stringa per la stampa dell'attributo
	 * 
	 * @return restituisce la stringa per la stampa dell'attributo
	 */
	public String toString() {
		return "[" + getIndex() + "]" + getName();
	}

}