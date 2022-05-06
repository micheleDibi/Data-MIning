package mining;
import data.*;

public class KNN {
	
	Data data;

	public KNN(Data trainingSet) {
		data = trainingSet;
	}
	
	public Double predict(Example e, int k) {
		Double prediction = data.avgClosest(e, k);	
		return prediction;
	}

}
