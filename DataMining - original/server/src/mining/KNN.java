package mining;

import java.io.*;

import data.Data;
import example.Example;

/**
 * gestisce l'algoritmo di regressione per il calcolo del target dell'esempio
 * inserito dall'utente
 */
public class KNN implements Serializable {

	/**
	 * attributo che contiene il training set prelevato
	 */
	private final Data data;

	/**
	 * costruttore per caricare il training set
	 * 
	 * @param trainingSet insieme di esempi su cui calcolare l'algoritmo di
	 *                    regressione
	 */
	public KNN(Data trainingSet) {
		data = trainingSet;
	}

	/**
	 * predizione del valore target dell'esempio inserito dall'utente
	 * 
	 * @param out ObjectOutputStream per inviare i dati al client
	 * @param in  ObjectInputStream per ricevere i dati dal client
	 * @return restituisce il valore target dell'esempio inserito dall'utente
	 *         calcolato mediante l'algoritmo di regressione
	 * @throws IOException            eccezione generata in caso di errata
	 *                                comunicazione con il client
	 * @throws ClassNotFoundException eccezione generata in caso di classe non
	 *                                trovata
	 * @throws ClassCastException     eccezione generata in caso di errato cast
	 */
	public double predict(ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException, ClassCastException {
		Example e = data.readExample(out, in);
		int k;
		out.writeObject("Inserisci valore k>=1:");
		k = (Integer) (in.readObject());
		return data.avgClosest(e, k);
	}

	/**
	 * procedura di salvataggio del training set prelevato in un file
	 * 
	 * @param fileName nome del file in cui salvare il training set
	 * @throws IOException eccezione generata in caso di errato salvataggio
	 */
	public void salva(String fileName) throws IOException {
		FileOutputStream outFile = new FileOutputStream(fileName);
		ObjectOutputStream outObject = new ObjectOutputStream(outFile);

		outObject.writeObject(this);
		outObject.close();
	}

	/**
	 * prelievo del training set da un file per eseguire l'algoritmo di regressione
	 * 
	 * @param fileName nome del file serializzato da cui prelevare il training set
	 * @return restituisce il training set prelavato da un file serializzato
	 * @throws IOException            eccezione generata in caso di errato prelievo
	 * @throws ClassNotFoundException eccezione generata in caso di classe non
	 *                                trovata
	 */
	public static KNN carica(String fileName) throws IOException, ClassNotFoundException {

		File suppFile = new File(fileName);

		if (!suppFile.isFile()) {
			throw new FileNotFoundException("File non trovato o e' una directory");
		}

		if (!fileName.substring(fileName.indexOf(".")).equals(".dmp")
				&& !fileName.substring(fileName.indexOf(".")).equals(".bin")) {
			throw new FileNotFoundException("Estensione del file non valida (accettati .bin o .dmp)");
		}

		FileInputStream inFile = new FileInputStream(fileName);
		try (ObjectInputStream inObject = new ObjectInputStream(inFile)) {
			return (KNN) inObject.readObject();
		}
	}

	/**
	 * restituisce sotto forma di stringa il training set
	 * 
	 * @return training set
	 */
	@Override
	public String toString() {
		return data.toString();
	}
}
