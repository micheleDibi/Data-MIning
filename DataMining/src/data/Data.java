package data;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Data {
	
	private Example[] data;
	private Double[] target;
	private int numberOfExamples;
	private Attribute[] explanatorySet;
	private ContinuousAttribute classAttribute;

	public Data(String fileName) throws TrainingDataException, FileNotFoundException {
		
		  File inFile = new File (fileName);
		  
		  if(!inFile.exists()) 
			  throw new TrainingDataException("File non trovato");
		  
		  Scanner sc = new Scanner (inFile);
	      String line = sc.nextLine().trim();
	      String[] s = line.split(" ");
	      
	      if(!s[0].contains("@schema"))
	    	  throw new TrainingDataException("Parametro @schema non dichiarato");
	      
	      if(s.length > 2) {
	    	  throw new TrainingDataException("Numero di parametri di @schema non valido");
	      } else if(s.length == 1) {  // s[0] = "@schema"; s[1] = "4";
	    	  	throw new TrainingDataException("Valore dello @schema non dichiatato");
	      } else {
	    	  boolean isNum = s[1].matches("[0-9]+");  //utilizzo di una espressione regolare TODO controllare se accetta valori decimali
	    	  
	    	  if (isNum == false) 
	    		  throw new TrainingDataException("Il valore inserito del parametro @schema non e' numerico");
	      }
	      
		  //popolare explanatory Set 
	  		
		  explanatorySet = new Attribute[new Integer(s[1])];
		  short iAttribute = 0;
	      line = sc.nextLine().trim();
	      
	      while(!line.contains("@data") && sc.hasNextLine()){
	    	  
	    	  s=line.split(" ");
	    	  
	    	  if(s[0].equals("@desc")) {
	    		  
	    		  if(s.length == 2) {
	    			  explanatorySet[iAttribute] = new DiscreteAttribute(s[1],iAttribute);
	    		  } else throw new TrainingDataException("Parametri @desc mancante");
  
		      }
	    	  else if(s[0].equals("@target")) {
	    		  
	    		  if(s.length == 2) {
	    			  classAttribute = new ContinuousAttribute(s[1], iAttribute);
	    		  } else throw new TrainingDataException("Parametro @target mancante");
	    	  
	    	  }	    			  
	    			  
	    	  iAttribute++;
	    	  line = sc.nextLine().trim();
	      }
	      
	      if(sc.hasNextLine() == false) {
	    	  throw new TrainingDataException("Parametro @data mancante");
	      }
	      
	      if(line.split(" ").length > 2) {
	    	  throw new TrainingDataException("Sinstatti parametro @data non corretta");
	    	  
	      } else if ((line.split(" ").length != 2) ){
	    	  throw new TrainingDataException("Valore parametro @data non specificato");
	      } else {
	    	  boolean isNum = line.split(" ")[1].matches("[0-9]+");  //utilizzo di una espressione regolare TODO controllare se accetta valori decimali
	      
	    	  if(isNum == false) {
	    		  throw new TrainingDataException("Il valore inserito del parametro @data non e' numerico");
	    	  }
	      }
	      		      
		  //avvalorare numero di esempi
	      numberOfExamples=new Integer(line.split(" ")[1]);
	       	      
	      //popolare data e target
	      data = new Example[numberOfExamples];
	      target = new Double[numberOfExamples];
	      	      	      
	      short iRow=0;
	      
	      while (sc.hasNextLine() && iRow < numberOfExamples)
	      {	    	  
	    	  Example e = new Example(getNumberofExplanatoryAttributes());
	    	  line = sc.nextLine().trim();
	    	  								// assumo che attributi siano tutti discreti
	    	  s = line.split(","); 			//E,E,5,4, 0.28125095
	    	  for(short jColumn=0;jColumn<s.length-1;jColumn++) {
	    		  e.set(s[jColumn],jColumn);
	    	  }
	    		  
	    	  data[iRow] = e;
	    	  target[iRow] = new Double(s[s.length-1]);
	    	  iRow++;
	      }
	      	      
	      if(sc.hasNextLine()) {
	    	  System.out.println("Numero di esempi maggiore di quanto dichiarato nel parametro @data");
	    	  System.out.println("Sono stati presi i primi " + numberOfExamples + " esempi");
	      }
	     
	      
		  sc.close();
	} // fine costruttore	
	
	int getNumberofExplanatoryAttributes() {
		return explanatorySet.length;
	}
	
	/*
	 * Partiziona data rispetto all'elemento x di key e restiutisce il punto di separazione
	 */
	private int partition(double key[], int inf, int sup){
		int i,j;
	
		i=inf; 
		j=sup; 
		int	med=(inf+sup)/2;
		
		Double x=key[med];
		
		data[inf].swap(data[med]);
		
		double temp=target[inf];
		target[inf]=target[med];
		target[med]=temp;
		
		temp=key[inf];
		key[inf]=key[med];
		key[med]=temp;
		
		while (true) 
		{
			
			while(i<=sup && key[i]<=x){ 
				i++; 
				
			}
		
			while(key[j]>x) {
				j--;
			
			}
			
			if(i<j) { 
				data[i].swap(data[j]);
				temp=target[i];
				target[i]=target[j];
				target[j]=temp;
				
				temp=key[i];
				key[i]=key[j];
				key[j]=temp;
				
				
			}
			else break;
		}
		data[inf].swap(data[j]);
		
		temp=target[inf];
		target[inf]=target[j];
		target[j]=temp;
		
		temp=key[inf];
		key[inf]=key[j];
		key[j]=temp;
		
		return j;

	}
	
	/*
	 * Algoritmo quicksort per l'ordinamento di data 
	 * usando come relazione d'ordine totale "<=" definita su key
	 * @param A
	 */
	private void quicksort(double key[], int inf, int sup){
		
		if(sup>=inf){
			
			int pos;
			
			pos=partition(key, inf, sup);
					
			if ((pos-inf) < (sup-pos+1)) {
				quicksort(key, inf, pos-1); 
				quicksort(key, pos+1,sup);
			}
			else
			{
				quicksort(key, pos+1, sup); 
				quicksort(key, inf, pos-1);
			}
			
			
		}
		
	}
	
	public double avgClosest(Example e, int k) {
		double[] key = new double[data.length];
		int count = 0;
		double somma = 0;
		
		for(int i = 0; i < data.length; i++) {
			key[i] = data[i].distance(e);
		}
		
		quicksort(key, 0, key.length - 1);
		
		for(int i = 0; i < target.length; i++) {
			if(key[i] < k) {
				somma = somma + target[i];
				count++;
			}
		}
		
		return somma / count;
		
	}
	
	public static void main(String args[])throws FileNotFoundException{
		//Data trainingSet = new Data("servo.dat");
		//System.out.println(trainingSet);	
	}

}
