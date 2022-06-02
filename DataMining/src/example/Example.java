package example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Example implements Serializable{
	
	private ArrayList<Object> example;

	public Example(int size) {
		example = new ArrayList<Object>(size);
	}
	
	public void set(Object o, int index) {
		
		if(index < example.size()) {
			example.set(index, o);
		} else {
			example.add(index, o);
		}
	}
	
	public Object get(int index) {
		return example.get(index);
	}
	
	public String toString() {
		Iterator<Object> it = example.iterator();
		String s = new String();
		
		while(it.hasNext()) {
			s = s + "oggetto: " + it.next().toString() + " ";
		}
		
		return s;	
	}
	
	
	//scambia i valori contenuti nel campo example dell'oggetto corrente 
	//con i valori contenuti nel campo example del parametro e
	public void swap(Example e) {
		
		if (this.example.size() != e.example.size())
			throw new ExampleSizeException("Dimensione dei due esempi passati differenti");
		
		for (int i = 0; i < this.example.size(); i++) {
			Object supp = e.get(i);		//oggetto di supporto per lo scambio
			e.set(this.get(i), i);
			this.set(supp, i);
		}
	}
	
	//calcola e restituisce la distanza di Hamming calcolata tra l’istanza di Example passata
	//come parametro e quella corrente
	public double distance(Example e) {
		
		if (this.example.size() != e.example.size())
			throw new ExampleSizeException("Dimensione dei due esempi passati differenti");
		
		double distance = 0;
		
		for	(int i = 0; i < this.example.size(); i++) {
			
			if(e.get(i) instanceof String) {
				if(!(e.get(i).equals(this.get(i)))) {
					distance++;
				}
			} else if(e.get(i) instanceof Double) {
				distance = distance + Math.abs((Double)this.get(i) - (Double)e.get(i));
			}
		}
		return distance;
	}
	
}
