package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import example.Example;
import utility.Keyboard;

public class Data implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Example> data;
	private ArrayList<Double> target;
	private int numberOfExamples;
	private ArrayList<Attribute> explanatorySet;
	private ContinuousAttribute classAttribute;

	public String toString() {
		String s = new String();
		
		s = s + "data: " + data.toString() + "\n";
		s = s + "target: " + target.toString() + "\n";
		s = s + "numberOfExamples: " + numberOfExamples + "\n";
		
		return s;
	}

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
		  explanatorySet = new ArrayList<Attribute>(new Integer(s[1]));
		  short iAttribute = 0;
	      line = sc.nextLine().trim();
	      
	      while(!line.contains("@data") && sc.hasNextLine()){
	    	  
	    	  s=line.split(" ");
	    	  
	    	  if(s[0].equals("@desc")) {
	    		  
	    		  if(s.length == 3) {
	    			  if(s[2].equalsIgnoreCase("discrete")) {
	    				  explanatorySet.add(iAttribute, new DiscreteAttribute(s[1],iAttribute));
	    			  } else if (s[2].equalsIgnoreCase("continuous")) {
	    				  explanatorySet.add(iAttribute, new ContinuousAttribute(s[1],iAttribute));
	    			  }
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
	      data = new ArrayList<Example>(numberOfExamples);
	      target = new ArrayList<Double>(numberOfExamples);
	      	      	      
	      short iRow=0;
	      
	      while (sc.hasNextLine() && iRow < numberOfExamples)
	      {	    	  
	    	  Example e = new Example(getNumberofExplanatoryAttributes());
	    	  line = sc.nextLine().trim();
	    	  							
	    	  s = line.split(",");
	    	  for(short jColumn=0;jColumn<s.length-1;jColumn++) {
	    		  
	    		  if(s[jColumn].matches("[0-9]+[\\.]?[0-9]*")) {
	    			  e.set(Double.parseDouble(s[jColumn]), jColumn);
	    		  } else {
	    			  e.set(s[jColumn],jColumn);
	    		  }
	    	  }
	    		 
	    	  data.add(iRow, e);
	    	  target.add(iRow, new Double(s[s.length-1]));
	    	  
	    	  int k = 0;
	    	  for (Attribute a : explanatorySet) {
	    		  if(a instanceof ContinuousAttribute) {
	    			  ((ContinuousAttribute) a).setMax((Double)e.get(k));
	    			  ((ContinuousAttribute) a).setMin((Double)e.get(k));
	    		  }
	    		  
	    		  k++;
	    	  }
	    	  
	    	  iRow++;
	      }
	      	      
	      if(sc.hasNextLine()) {
	    	  System.out.println("Numero di esempi maggiore di quanto dichiarato nel parametro @data");
	    	  System.out.println("Sono stati presi i primi " + numberOfExamples + " esempi");
	      }
	     
	      //System.out.println(classAttribute);
	      
		  sc.close();
	} // fine costruttore	
	
	int getNumberofExplanatoryAttributes() {
		return explanatorySet.size();
	}
	
	/*
	 * Partiziona data rispetto all'elemento x di key e restiutisce il punto di separazione
	 */
	private int partition(ArrayList<Double> key, int inf, int sup){
		int i = inf,j = sup;
		int	med=(inf + sup) / 2;
		
		Double x = key.get(med);
		
		data.get(inf).swap(data.get(med));
		
		double temp = target.get(inf);
		target.set(inf, target.get(med));
		target.set(med, temp);
		
		temp = key.get(inf);
		key.set(inf, key.get(med));
		key.set(med, temp);
		
		while (true) 
		{
			
			while(i <= sup &&  key.get(i) <= x){ 
				i++; 	
			}
		
			while(key.get(j) > x) {
				j--;
			}
			
			if(i<j) { 
				data.get(i).swap(data.get(j));
				
				temp = target.get(i);
				target.set(i, target.get(j));
				target.set(j, temp);
				
				temp = key.get(i);
				key.set(i, key.get(j));
				key.set(j, temp);
				
			}
			else break;
		}
		
		data.get(inf).swap(data.get(j));
		
		temp = target.get(inf);
		target.set(inf, target.get(j));
		target.set(j, temp);
		
		temp = key.get(inf);
		key.set(inf, key.get(j));
		key.set(j, temp);
		
		return j;

	}
	
	/*
	 * Algoritmo quicksort per l'ordinamento di data 
	 * usando come relazione d'ordine totale "<=" definita su key
	 * @param A
	 */
	private void quicksort(ArrayList<Double> key, int inf, int sup){
		
		if(sup>=inf){
			
			int pos;
			
			pos = partition(key, inf, sup);
					
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
		int diff = 0;
		double somma = 0;		
		ArrayList<Double> key = new ArrayList<Double>(data.size());
		
		e = scaledExample(e);
		
		Iterator<Example> ex = data.iterator();
		while(ex.hasNext()) {
			Example ecsample = ex.next();
			key.add(scaledExample(ecsample).distance(e));
		}
		
		quicksort(key, 0, key.size() - 1);
		
		Double min = key.get(0);
		int i = 0;
		for(i = 0; i < target.size() && diff < k; i++) {
			if(key.get(i).equals(min)) {
				somma = somma + target.get(i);
			} else {
				diff++;
				min = key.get(i);
				i--;
			}
		}
		
		return somma / (i);
	}
	
	public Example readExample() {
		Example e = new Example(getNumberofExplanatoryAttributes());
		int i = 0;
		for(Attribute a : explanatorySet) {
			if(a instanceof DiscreteAttribute) {
				System.out.print("Inserisci valore discreto X["+i+"]:");
				e.set(Keyboard.readString(),i);
			}
			else {
				double x = 0.0;
				do{
					System.out.print("Inserisci valore continuo X["+i+"]:");
					x = Keyboard.readDouble();
				} while(new Double(x).equals(Double.NaN));
				e.set(x,i);
			}
			i++;
		}
		return e;
	}
	
	private Example scaledExample(Example e) {
		int i = 0;
		Example supp = new Example(getNumberofExplanatoryAttributes());
		
		for(Attribute a : explanatorySet) {
			if(a instanceof ContinuousAttribute) {
				Double scaled = ((ContinuousAttribute) a).scale(Double.parseDouble(e.get(i).toString()));
				supp.set(scaled, i);
			} else if (a instanceof DiscreteAttribute) {
				supp.set(e.get(i), i);
			}
			
			i++;
		}
		
		return supp;
	}

}
