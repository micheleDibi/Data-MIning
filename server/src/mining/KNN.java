package mining;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.Data;
import example.Example;
import utility.Keyboard;

public class KNN implements Serializable{
	
	Data data;

	public KNN(Data trainingSet) {
		data = trainingSet;
	}

	/* non utilizzati
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
	*/

	public double predict (ObjectOutputStream out, ObjectInputStream in) throws
			IOException, ClassNotFoundException, ClassCastException
	{
		Example e = data.readExample(out, in);
		int k;
		out.writeObject("Inserisci valore k>=1:");
		k = (Integer)(in.readObject());
		return data.avgClosest(e, k);
	}
	
	public void salva(String fileName) throws IOException  {
		FileOutputStream outFile = new FileOutputStream(fileName);
		ObjectOutputStream outObject = new ObjectOutputStream(outFile);
		
		outObject.writeObject(this);
		outObject.close();
	}
	
	public static KNN carica(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream inFile = new FileInputStream(fileName);
		try (ObjectInputStream inObject = new ObjectInputStream(inFile)) {
			KNN k = (KNN) inObject.readObject();
			
			return k;
		}
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
