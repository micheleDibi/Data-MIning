package database;

import java.io.Serial;

/**
 * Gestione dell'eccezione in caso di mancata connessione con il DB
 */
public class DatabaseConnectionException extends Exception {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * costruttore per invio messaggio personalizzato
	 * 
	 * @param msg messaggio personalizzato da inviare come eccezione
	 */
	DatabaseConnectionException(String msg) {
		super(msg);
	}
}
