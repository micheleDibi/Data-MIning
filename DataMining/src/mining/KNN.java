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
		
		System.out.println("Inserire il numero di attributi da inserire");
		int numOfAttribute = Keyboard.readInt();
		Example e = new Example(numOfAttribute);
		
		for(int i = 0; i < numOfAttribute; i++) {
			System.out.println("inserire attributo " + i + ": ");
			String inputUser =  Keyboard.readString().trim();
			
			if(inputUser.matches("[0-9]+[\\.]?[0-9]*")) {
				e.set((Double.parseDouble(inputUser)), i);
			}
			
			
			e.set(Keyboard.readString(), i);
		}
		
		int k = Keyboard.readInt();
		
		Double prediction = data.avgClosest(e, k);
		return prediction;
	}

}
