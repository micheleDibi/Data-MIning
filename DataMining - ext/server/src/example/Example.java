package example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * gestisce gli esempi dei training set
 */
public class Example implements Serializable {

	/**
	 * lista di esempi in cui un esempio corrisponde ad una riga della tabella
	 */
	private final ArrayList<Object> example;

	/**
	 * inizializza la lista di esempi con una determinata dimensione
	 * 
	 * @param size dimensione della lista
	 */
	public Example(int size) {
		example = new ArrayList<>(size);
	}

	/**
	 * Inserisce l'oggetto in una determinata posizione della lista
	 * 
	 * @param o     oggetto da inserire nella lista
	 * @param index posizione all'interno della lista in cui inserire l'oggetto
	 */
	public void set(Object o, int index) {

		if (index < example.size()) {
			example.set(index, o);
		} else {
			example.add(index, o);
		}
	}

	/**
	 * Restituisce l'oggetto presente in una determinata posizione
	 * 
	 * @param index posizione dell'elemento presente nella lista da prelevare
	 * @return Restituisce l'oggetto presente in una determinata posizione
	 */
	public Object get(int index) {
		return example.get(index);
	}

	/**
	 * Restuisce sotto forma di stringa l'elenco di tutti i valori presenti nella
	 * lista
	 * 
	 * @return Restuisce sotto forma di stringa l'elenco di tutti i valori presenti
	 *         nella lista
	 */
	public String toString() {
		Iterator<Object> it = example.iterator();
		StringBuilder s = new StringBuilder();

		while (it.hasNext()) {
			s.append("oggetto: ").append(it.next().toString()).append(" ");
		}

		return s.toString();
	}

	/**
	 * scambia i valori contenuti nel campo example dell'oggetto corrente con i
	 * valori contenuti nel campo example del parametro e
	 * 
	 * @param e esempio con cui effettuare lo scambio
	 */
	public void swap(Example e) {

		if (this.example.size() != e.example.size())
			throw new ExampleSizeException("Dimensione dei due esempi passati differenti");

		for (int i = 0; i < this.example.size(); i++) {
			Object supp = e.get(i); // oggetto di supporto per lo scambio
			e.set(this.get(i), i);
			this.set(supp, i);
		}
	}

	/**
	 * calcola e restituisce la distanza (di Hamming se passato esempio discreto -
	 * min-max scaler per esempio continuo) calcolata tra l’istanza di Example
	 * passata come parametro e quella corrente
	 * 
	 * @param e esempio con cui calcolare da distanza
	 * @return restitusce la distanza calcolata
	 */
	public double distance(Example e) {

		if (this.example.size() != e.example.size())
			throw new ExampleSizeException("Dimensione dei due esempi passati differenti");

		double distance = 0;

		for (int i = 0; i < this.example.size(); i++) {

			if (e.get(i) instanceof String) {
				if (!(e.get(i).equals(this.get(i)))) {
					distance++;
				}
			} else if (e.get(i) instanceof Double) {
				distance = distance + Math.abs((Double) this.get(i) - (Double) e.get(i));
			}
		}
		return distance;
	}

}
