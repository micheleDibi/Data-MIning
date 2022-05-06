import java.io.FileNotFoundException;
import data.*;
import mining.*;


class MainTest {

	public static void main(String[] args) throws FileNotFoundException{
		//Data trainingSet= new Data("servo.dat");
		Data trainingSet = new Data("./src/simple.dat");
		System.out.println(trainingSet);
		
		KNN knn = new KNN(trainingSet);
		
		Example e = new Example(2);
		e.set("A",0);
		e.set("B",1);
		System.out.println("Prediction with K=1:" + knn.predict(e, 1));
		System.out.println("Prediction with K=2:" + knn.predict(e, 2));
		System.out.println("Prediction with K=3:" + knn.predict(e, 3));
		System.out.println("Prediction with K=4:" + knn.predict(e, 4));

	}
	
}
