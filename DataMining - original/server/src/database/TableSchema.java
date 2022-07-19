package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce le colonne su cui effettuare / e' stata effettuata la query
 * definendo nome e tipo
 */
public class TableSchema implements Iterable<Column> {

	/**
	 * lista di colonne degli esempi della tabella. Attenzione! La colonna non
	 * contiene i valori ma è caratterizzata esclusivamente dal nome e dal tipo.
	 */
	private final List<Column> tableSchema = new ArrayList<Column>();
	/**
	 * colonna che contiene i target dei corrispettivi esempi. Attenzione! La
	 * colonna non contiene i valori ma è caratterizzata esclusivamente dal nome e
	 * dal tipo.
	 */
	private Column target;
	/**
	 * nome della tabella di cui definire le colonne
	 */
	private final String tableName;

	/**
	 * Preleva, per ciascuna colonna della tabella inserita, il nome ed il tipo,
	 * definendo dunque se una colonna corrisponde
	 * ad un attributo continuo o discreto, e preleva anche la colonna target.
	 *
	 * @param tableName nome della tabella da cui prelevare i dati
	 * @param db        database da cui prelevare i dati in cui è presente la
	 *                  tabella inserita
	 * @throws SQLException                      eccezione generata in caso di
	 *                                           errata connessione con il database
	 * @throws InsufficientColumnNumberException eccezione generata in caso di
	 *                                           numero di colonne della tabella
	 *                                           selezionata minore di 2
	 */
	public TableSchema(String tableName, DbAccess db) throws SQLException, InsufficientColumnNumberException {
		this.tableName = tableName;

		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();

		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		if (tableExists(db, tableName)) {

			DatabaseMetaData meta = db.getConnection().getMetaData();
			ResultSet res = meta.getColumns(null, null, tableName, null);

			while (res.next()) {

				if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
					if (res.isLast()) {
						target = new Column(
								res.getString("COLUMN_NAME"),
								mapSQL_JAVATypes.get(res.getString("TYPE_NAME")));
					} else {
						tableSchema.add(new Column(
								res.getString("COLUMN_NAME"),
								mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
					}

			}

			res.close();

		} else
			throw new SQLException("[SQLException]: la tabella inserita non esiste;");

		if (target == null || tableSchema.size() <= 1)
			throw new InsufficientColumnNumberException(
					"[InsufficientColumnNumberException]: La tabella selezionata contiene meno di due colonne");
	}

	/**
	 * restituisce la colonna contenente i target. Attenzione! La colonna non
	 * contiene i valori ma è caratterizzata esclusivamente dal nome e dal tipo.
	 * 
	 * @return restituisce la colonna contenente i target
	 */
	Column target() {
		return target;
	}

	/**
	 * restituisce il numero di colonne presenti nella tabella
	 * 
	 * @return restituisce il numero di colonne presenti nella tabella
	 */
	int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * restituisce il nome della tabella da cui prelevare le colonne
	 * 
	 * @return
	 */
	String getTableName() {
		return tableName;
	}

	/**
	 * restituisce l'iteratore delle colonne della tabella.
	 * 
	 * @return Iteratore delle colonne
	 */
	@Override
	public Iterator<Column> iterator() {
		return tableSchema.iterator();
	}

	private boolean tableExists(DbAccess db, String tableName) throws SQLException {
		DatabaseMetaData meta = db.getConnection().getMetaData();
		ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
		return resultSet.next();
	}
}
