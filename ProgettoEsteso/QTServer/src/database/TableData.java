package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;

import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 * Classe che modella l'insieme di transazioni collezionate in una tabella
 */
public class TableData {

    /**
     * Gestore della connessione al database
     */
    DbAccess db;

    /**
     * Costruisce la classe TableData inizializzando il gestore della connessione al
     * database
     * 
     * @param db gestore del database a cui accedere
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Restituisce una lista contenente le tuple distinte lette dalla tabella
     * specificata nel database
     * 
     * @param table nome della tabella
     * @return lista delle tuple lette dalla tabella
     * @throws SQLException      se non si riesce a leggere il contenuto della
     *                           tabella dal database
     * @throws EmptySetException se il resultset è vuoto
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
        LinkedList<Example> transSet = new LinkedList<Example>();
        Statement statement;

        TableSchema tSchema = new TableSchema(db, table);
        // System.out.println(tSchema.toString());

        String query = "select distinct ";

        for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
            Column c = tSchema.getColumn(i);
            if (i > 0)
                query += ",";
            query += c.getColumnName();
        }
        if (tSchema.getNumberOfAttributes() == 0) {
            throw new SQLException();

        }
        query += (" FROM " + table);

        statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        boolean empty = true;
        while (rs.next()) {
            empty = false;
            Example currentTuple = new Example();
            for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
                if (tSchema.getColumn(i).isNumber())
                    currentTuple.add(rs.getDouble(i + 1));
                else
                    currentTuple.add(rs.getString(i + 1));
            transSet.add(currentTuple);
        }
        rs.close();
        statement.close();
        if (empty) {
            throw new EmptySetException();
        }

        return transSet;

    }

    /**
     * Restituisce l'insieme di valori distinti e ordinati (in maniera crescente)
     * letti dalla tabella nella colonna specificata
     * 
     * @param table  nome della tabella da cui leggere gli elementi
     * @param column Colonna da cui leggere i valori
     * @return insieme dei valori presenti nella colonna
     * @throws SQLException se non si riesce a leggere il contenuto della
     *                      tabella dal database
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        Set<Object> valueSet = new TreeSet<Object>();
        Statement statement;
        // TableSchema tSchema = new TableSchema(db, table);

        String query = "select distinct ";

        query += column.getColumnName();

        query += (" FROM " + table);

        query += (" ORDER BY " + column.getColumnName());

        statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            if (column.isNumber())
                valueSet.add(rs.getDouble(1));
            else
                valueSet.add(rs.getString(1));

        }
        rs.close();
        statement.close();

        return valueSet;

    }

    /**
     * Estrae il valore massimo (o minimo) dalla tabella nella colonna specificata
     * 
     * @param table     nome della tabella in cui cercare
     * @param column    colonna in cui cercare il massimo o il minimo
     * @param aggregate modalità con cui cercare il valore (specifica se cercare il
     *                  massimo o il minimo)
     * @return l'elemento cercato (che è il più grande o il più piccolo)
     * @throws SQLException     se non si riesce a leggere il contenuto della
     *                          tabella dal database
     * @throws NoValueException se il resultset è vuoto o il valore calcolato è pari
     *                          a null
     */
    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
            throws SQLException, NoValueException {
        Statement statement;
        // TableSchema tSchema = new TableSchema(db, table);
        Object value = null;
        String aggregateOp = "";

        String query = "select ";
        if (aggregate == QUERY_TYPE.MAX)
            aggregateOp += "max";
        else
            aggregateOp += "min";
        query += aggregateOp + "(" + column.getColumnName() + ") FROM " + table;

        statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            if (column.isNumber())
                value = rs.getFloat(1);
            else
                value = rs.getString(1);

        }
        rs.close();
        statement.close();
        if (value == null)
            throw new NoValueException("No " + aggregateOp + " on " + column.getColumnName());

        return value;

    }

}
