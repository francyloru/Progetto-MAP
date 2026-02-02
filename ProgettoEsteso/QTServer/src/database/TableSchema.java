package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che modella lo scehma della tabella letta dal database
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class TableSchema {
    DbAccess db;

    /**
     * Classe che modella una singola colonna della tabella
     */
    public class Column {

        /**
         * Stringa rappresentante il nome della singola colonna
         */
        private String name;

        /**
         * Stringa che descrive il tipo di dato utilizzato nella singola colonna
         */
        private String type;

        /**
         * Costruisce la classe colonna (descritta da un nome e il tipo di dato usato)
         * 
         * @param name nome della colonna
         * @param type tipo di dato utilizzato nella colonna
         */
        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Restituisce il nome della colonna
         * 
         * @return nome della colonna
         */
        public String getColumnName() {
            return name;
        }

        /**
         * Restituisce un valore booleano che dice se il tipo di dato contenuto nella
         * colonna è "number" (che va poi interpretato come un int o double)
         * 
         * @return true se il tipo della colonna è "number"
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * Restituisce una stringa che descrive lo stato della colonna, intesa come il
         * nome della colonna seguita dal tipo di dato in essa contenuta
         * 
         * @return stato della colonna
         */
        @Override
        public String toString() {
            return name + ":" + type;
        }
    }

    /**
     * Schema della tabella inteso come sequenza delle colonne in essa contenute
     */
    List<Column> tableSchema = new ArrayList<Column>();

    /**
     * Costruttore della classe TableSchema che analizza e memorizza la struttura
     * della tabella del database. Come risultato, l'attributo TableSchema
     * contiene una rappresentazione della struttura della tabella con i nomi delle
     * colonne e i tipi di dati semplificati (string o number)
     * 
     * @param db        gestore della connessione al database da cui leggere lo
     *                  schema della tabella interessata
     * @param tableName nome della tabella da cui leggere lo schema
     * @throws SQLException se non si riesce a leggere il contenuto della tabella
     *                      dal database
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException {
        this.db = db;
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
        // http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        Connection con = db.getConnection();
        // permette di ottenere info su schema, colonne, ecc
        DatabaseMetaData meta = con.getMetaData();

        // System.out.println(con.isClosed() == true);
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {

            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));

        }
        res.close();

    }

    /**
     * Restituisce il numero di colonne dello schema della tabella
     * 
     * @return numero di colonne della tabella
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna (intesa come coppia di nome attributo e tipo
     * rappresentato) della tabella nella posizione passata come parametro
     * 
     * @param index indice della colonna desiderata
     * @return colonna in posizione index
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }

    /**
     * Restituisce lo schema dell'intera tabella sotto forma di stringa
     * 
     * @return stringa rappresentante lo schema della tabella
     */
    @Override
    public String toString() {
        String schema = new String();
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            schema = schema + getColumn(i) + "\n";
        }
        return schema;
    }

}
