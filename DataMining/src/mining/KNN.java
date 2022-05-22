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
	
	public double predict () {
		Example e = data.readExample();
		int k=0;
		do {
			System.out.print("Inserisci valore k>=1:");
			k=Keyboard.readInt();
		}while (k < 1);
		return data.avgClosest(e, k);
	}

}
