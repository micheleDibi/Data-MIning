package database;

import java.io.Serial;

/**
 * Gestisce l'eccezione in caso di un numero insufficente di colonne presenti
 * nella tabella
 * da cui prelevare il training set (min 2)
 */

public class InsufficientColumnNumberException extends Exception {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * costruttore per invio messaggio personalizzato
	 * 
	 * @param msg messaggio personalizzato da inviare come eccezione
	 */
	InsufficientColumnNumberException(String msg) {
		super(msg);
	}
}
