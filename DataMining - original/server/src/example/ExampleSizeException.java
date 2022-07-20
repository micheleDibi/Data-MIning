package example;

import java.io.Serial;

/**
 * eccezione per il controllo della dimensione uguale degli esempi su cui
 * effettuare le operazioni
 */

public class ExampleSizeException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * costruttore per invio messaggio personalizzato
	 * 
	 * @param msg messaggio personalizzato da inviare come eccezione
	 */
	public ExampleSizeException(String msg) {
		super(msg);
	}

}
