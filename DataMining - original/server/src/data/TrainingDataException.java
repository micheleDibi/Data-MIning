package data;

import java.io.Serial;

/**
 * Gestione dell'eccezione nel caso di incorrettezza dello schema nel file del
 * training set o di parametri mancanti
 */
public class TrainingDataException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * costruttore per invio messaggio personalizzato
	 * 
	 * @param message messaggio personalizzato da inviare come eccezione
	 */
	TrainingDataException(String message) {
		super(message);
	}

}
