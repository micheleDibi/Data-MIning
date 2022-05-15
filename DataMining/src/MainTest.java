import java.io.FileNotFoundException;

import data.Data;
import data.Example;
import data.ExampleSizeException;
import data.TrainingDataException;
import mining.KNN;

public class MainTest {

	public static void main(String[] args) throws FileNotFoundException {
				
		try {
			
			Data trainingSet = new Data("./src/simple.dat");
			System.out.println(trainingSet);
			
			KNN knn = new KNN(trainingSet);
			
			Example e = new Example(2);
			e.set("A",0);
			e.set("B",1);
			
			try {
				System.out.println("Prediction with K=1: " + knn.predict(e, 1));
			} catch (ExampleSizeException exc) {
				System.out.println(exc.getMessage());
			}
	
			try { 
				System.out.println("Prediction with K=2: " + knn.predict(e, 2));
			} catch (ExampleSizeException exc) {
				System.out.println(exc.getMessage());		
			}
			
			try{
				System.out.println("Prediction with K=3: " + knn.predict(e, 3));
			} catch (ExampleSizeException exc) {
				System.out.println(exc.getMessage());		
			}
			
			try{
				System.out.println("Prediction with K=4: " + knn.predict(e, 4));
			} catch (ExampleSizeException exc) {
				System.out.println(exc.getMessage());		
			}
			
			/*
			
			e = new Example(3);
			e.set("A",0);
			e.set("B",1);
			e.set("C", 2);
			
			try {
				System.out.println("Prediction with K=1: " + knn.predict(e, 2));
			} catch (ExampleSizeException exc) {
				System.out.println(exc.getMessage());		
			}

			// read example withKeyboard
			System.out.println("Prediction:" + knn.predict());
			*/
		}
		catch(TrainingDataException exc){
			System.out.println(exc.getMessage());		
		}

	}

}
