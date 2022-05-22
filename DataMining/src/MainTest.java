import java.io.FileNotFoundException;

import data.Data;
import data.Example;
import data.ExampleSizeException;
import data.TrainingDataException;
import mining.KNN;
import utility.Keyboard;

public class MainTest {

	public static void main(String[] args) throws FileNotFoundException{
		try {
			Data trainingSet = new Data("./src/provaC.dat");
			
			System.out.println(trainingSet);
			
			KNN knn = new KNN(trainingSet);
			
			String r;
			do {
				// read example withKeyboard
				System.out.println("Prediction:" + knn.predict());
				System.out.println("Vuoi ripetere? Y/N");
				
				r = Keyboard.readString();
			}while (r.toLowerCase().equals("y"));
		}
		catch(TrainingDataException exc){System.out.println(exc.getMessage());}
	}

}
