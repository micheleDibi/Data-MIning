package database;

/**
 * Gestisce le colonne della tabella da cui prelevare il training set
 */

public class Column {

	/**
	 * nome della colonna
	 */
	private final String name;

	/**
	 * tipo della colonna
	 */
	private final String type;

	/**
	 * costruttore per definire nome e tipo della colonna del database
	 * 
	 * @param name nome della colonna del database
	 * @param type tipo della colonna del database
	 */
	Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * restituisce il nome della colonna
	 * 
	 * @return restituisce il nome della colonna
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 * verifica che la colonna sia numerica
	 * 
	 * @return true se la colonna e' numerica, false altrimenti
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	/**
	 * restituisce una stringa contenente il nome e il tipo della colonna
	 * corrispondente
	 * 
	 * @return restituisce una stringa contenente il nome e il tipo della colonna
	 *         corrispondente
	 */
	public String toString() {
		return name + ": " + type;
	}
}