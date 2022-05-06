package data;
public class Example {
	
	private Object[] example;

	public Example(int size) {
		example = new Object[size];
	}
	
	public void set(Object o, int index) {
		example[index] = o;
	}
	
	Object get(int index) {
		return example[index];
	}
	
	
	//scambia i valori contenuti nel campo example dell'oggetto corrente 
	//con i valori contenuti nel campo example del parametro e
	void swap(Example e) {
		for (int i = 0; i < this.example.length; i++) {
			Object supp = e.get(i);		//oggetto di supporto per lo scambio
			e.set(this.get(i), i);
			this.set(supp, i);
		}
	}
	
	//calcola e restituisce la distanza di Hamming calcolata tra l’istanza di Example passata
	//come parametro e quella corrente
	double distance(Example e) {
		double distance = 0;
		
		for	(int i = 0; i < this.example.length; i++) {
			if(!(e.get(i).equals(this.get(i)))) {
				distance++;
			}
		}
	
		return distance;
	}
	
}
