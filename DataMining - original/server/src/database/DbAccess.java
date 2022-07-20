package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestisce l'accesso al DB per la lettura dei dati di training
 */
public class DbAccess {

	/**
	 * driver per la connessione al database
	 */
	private final String DBMS = "jdbc:mysql";
	/**
	 * Server in cui è presente il database a cui connettersi
	 */
	private final String SERVER = "localhost";
	/**
	 * porta del server a cui collegarsi per accedere al database
	 */
	private final int PORT = 3306;
	/**
	 * nome del database a cui collegarsi
	 */
	private final String DATABASE = "Map";
	/**
	 * username dell'utente con cui collegarsi al database
	 */
	private final String USER_ID = "Student";
	/**
	 * password dell'utente
	 */
	private final String PASSWORD = "map";

	/**
	 * connessione con il database
	 */
	private final Connection conn;

	/**
	 * Inizializza una connessione al DB
	 * 
	 * @throws DatabaseConnectionException eccezione generata in caso di errata
	 *                                     connessione con il database
	 */
	public DbAccess() throws DatabaseConnectionException {
		String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
				+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
		existsDatabase();

		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		} catch (SQLException e) {
			System.out.println("[DbAccess_DbAccess_SQLException]: Impossibile connettersi al DB, " + e.getMessage());
			throw new DatabaseConnectionException(e.getMessage());
		}
	}

	private void existsDatabase() throws DatabaseConnectionException {
		String connectionString = DBMS + "://" + SERVER + ":" + PORT;

		try {
			Connection c = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
			String query = "CREATE DATABASE IF NOT EXISTS " + DATABASE;

			Statement st = c.createStatement();
			st.executeUpdate(query);

			st.close();
			c.close();

		} catch (SQLException e) {
			System.out.println("[DbAccess_existsDatabase_SQLException]: " + e.getMessage());
			throw new DatabaseConnectionException(e.getMessage());
		}
	}

	/**
	 * restituisce la connessione al DB
	 * 
	 * @return restituisce la connessione con il database
	 */
	Connection getConnection() {
		return conn;
	}

	/**
	 * chiude la connessione con il DB
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(
					"[DbAccess_closeConnection_SQLException] : Impossibile chiudere la connessione" + e.getMessage());
		}
	}
}
