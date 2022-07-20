package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import example.Example;

/**
 * Classe che gestisce il prelievo dei dati e avvaloramento del training set
 */
public class TableData {

	/**
	 * attributo per la connessione con il database
	 */
	private final DbAccess db;
	/**
	 * nome della tabella da cui prelevare il training set
	 */
	private final String table;
	/**
	 * schema della tabella (ossia le colonne della tabella)
	 */
	private final TableSchema tSchema;
	/**
	 * lista di esempi prelevati
	 */
	private final List<Example> transSet;
	/**
	 * lista dei target dei corrispettivi esempi
	 */
	private final List target;

	/**
	 * Esegue la query per prelevare i dati avvalorando il training set e il target
	 * 
	 * @param db      database a cui fare riferimento per eseguire la query
	 * @param tSchema nome della tabella su cui eseguire la query
	 * @throws SQLException eccezione sollevata nel caso di mancata connessione al
	 *                      database
	 * @throws NoValueException eccezione sollevata in caso di null value prelevato dalla tabella
	 */
	public TableData(DbAccess db, TableSchema tSchema) throws SQLException, NoValueException {

		this.db = db;
		this.tSchema = tSchema;
		this.table = tSchema.getTableName();
		transSet = new ArrayList<>();
		target = new ArrayList<>();
		init();
	}

	private void init() throws SQLException, NoValueException {
		StringBuilder query = new StringBuilder("select ");

		for (Column c : tSchema) {
			query.append(c.getColumnName());
			query.append(",");
		}
		query.append(tSchema.target().getColumnName());
		query.append(" FROM ").append(table);

		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query.toString());

		if (!rs.isBeforeFirst()) {
			throw new NoValueException("Non sono presenti elementi nella tabella");
		}

		while (rs.next()) {
			Example currentTuple = new Example(tSchema.getNumberOfAttributes());
			int i = 0;

			for (Column c : tSchema) {

				if (rs.getObject(i + 1) != null) {
					if (c.isNumber())
						currentTuple.set(rs.getDouble(i + 1), i);
					else
						currentTuple.set(rs.getString(i + 1), i);
					i++;
				} else {
					throw new NoValueException("Una riga della tabella ha un valore null");
				}
			}

			transSet.add(currentTuple);

			if (tSchema.target().isNumber())
				target.add(rs.getDouble(tSchema.target().getColumnName()));
			else
				target.add(rs.getString(tSchema.target().getColumnName()));

		}
		rs.close();
		statement.close();
	}

	/**
	 * restituisce la lista di esempi prelevati
	 * 
	 * @return restituisce la lista di esempi prelevati
	 */
	public List<Example> getExamples() {
		return transSet;
	}

	/**
	 * restituisce la lista dei target dei corrispettivi esempi
	 * 
	 * @return restituisce la lista dei target dei corrispettivi esempi
	 */
	public List getTargetValues() {
		return target;
	}

	/**
	 * funzione per la definizione del valore minimo / massimo di una colonna
	 * 
	 * @param column    colonna di cui definire il valore minimo / massimo
	 * @param aggregate parametro per definire se prelevare il valore minimo o il
	 *                  valore massimo
	 * @return restituisce il valore minimo / massimo della colonna inserita
	 * @throws SQLException eccezione generata in caso di errata connessione con il
	 *                      database
	 */
	public Double getAggregateColumnValue(Column column, QUERY_TYPE aggregate) throws SQLException {
		double value = 0;
		String query = "select " + aggregate + "(" + column.getColumnName() + ")" + " from " + table;

		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);

		while (rs.next()) {
			value = rs.getDouble(1);
		}

		return value;
	}

}
