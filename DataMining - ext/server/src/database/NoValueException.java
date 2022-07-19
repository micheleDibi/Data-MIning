package database;

import java.io.Serial;

/**
 * Gestisce l'eccezione nel caso in cui un valore di una tupla (riga della
 * tabella) sia null
 */

public class NoValueException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * costruttore per invio messaggio personalizzato
	 * 
	 * @param msg messaggio personalizzato da inviare come eccezione
	 */
	public NoValueException(String msg) {
		super(msg);
	}

}
