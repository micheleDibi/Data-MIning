package mining;

import data.Data;
import data.Example;

import utility.Keyboard;

public class KNN {
	
	Data data;

	public KNN(Data trainingSet) {
		data = trainingSet;
	}
	
	public Double predict(Example e, int k) {
		Double prediction = data.avgClosest(e, k);	
		return prediction;
	}
	
	public Double predict() {
		
		//2, A, B, 2
		System.out.println("Inserire il numero di attributi da inserire: ");
		int numOfAttribute = Keyboard.readInt();
		System.out.println("Numero di attributi inserito: " + numOfAttribute);
		
		Example e = new Example(numOfAttribute);
		
		System.out.println("Inizio inserimento attributi...");
		for(int i = 0; i < numOfAttribute; i++) {
			System.out.println("Inserire attributo " + i + ": ");
			String inputUser =  Keyboard.readString().trim();
			e.set(inputUser, i);
			
			System.out.println("valori inseriti...");
			System.out.print("<");
			for(int j = 0; j < numOfAttribute; j++) {
				
				if(e.get(j) != null) {
					System.out.print(e.get(j) + ",");
				}
				
			}
			System.out.print(">");
			System.out.println("fine stampa valori inseriti.");
		}
		System.out.println("fine inserimento attributi.");
		
		System.out.println("Inserire il valore k: ");
		int k = Keyboard.readInt();
		System.out.println("valore k inserito: " + k);
		
		System.out.println("Inizio calcolo della media...");
		Double prediction = data.avgClosest(e, k);
		System.out.println("media calcolata, procedo con il return...");
		
		return prediction;
	}

}
