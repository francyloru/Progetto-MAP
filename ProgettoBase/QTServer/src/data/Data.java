package data;

import java.sql.SQLException;
import java.util.*;

import database.*;

/**
 * Classe che modella l'insieme di transazioni (cioè l'insieme di tuple)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class Data {

    /**
     * Matrice in cui ogni riga modella una transazione descritta dagli
     * attributi riportati sulle colonne
     */
    private List<Example> data;

    /**
     * Cardinalità dell'insieme di transazioni (cioè il numero di righe della
     * tabella)
     */
    private int numberOfExamples;

    /**
     * Lista degli attributi che descrivono i campi della tupla (questo descrive gli
     * attributi, cioè <strong>nome e dominio dei valori rappresentabili</strong>,
     * associati alle singole colonne della matrice)
     */
    private List<Attribute> attributeSet;

    /**
     * Costruisce dinamicamente la classe Data (contenente sia la struttura che le
     * tuple) leggendo, dal database, la tabella con nome pari a quello passato come
     * parametro
     * 
     * @param tabella nome della tabella da leggere dal database
     * @throws DatabaseConnectionException se la connessione con il database non è
     *                                     andata a buon fine
     * @throws EmptySetException           se non è stato possibile leggere le tuple
     *                                     e la struttura della tabella dal database
     */
    public Data(String tabella) throws DatabaseConnectionException, EmptySetException {
        data = new ArrayList<>();
        attributeSet = new LinkedList<>();
        numberOfExamples = 0;
        DbAccess database = new DbAccess();

        try {
            synchronized (this) {
                database.initConnection();// se la connessione non avviene, viene espulsa l'eccezione e basta

                // vengono caricate le tuple contenute nella tabella
                TableData dataset = new TableData(database);
                data = dataset.getDistinctTransazioni(tabella);// _>
                numberOfExamples = data.size();

                // viene caricato la struttura della tabella
                TableSchema schema = new TableSchema(database, tabella);// _>
                for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                    Set<Object> valori_grezzi = dataset.getDistinctColumnValues(tabella, schema.getColumn(i));
                    String[] valori = new String[valori_grezzi.size()];

                    if (!schema.getColumn(i).isNumber()) {
                        int j = 0;
                        for (Object elemento : valori_grezzi) {
                            valori[j] = elemento.toString();
                            j++;
                        }
                        attributeSet.add(new DiscreteAttribute(schema.getColumn(i).getColumnName(), i, valori));
                    } else {

                        try {
                            attributeSet.add(new ContinuousAttribute(schema.getColumn(i).getColumnName(), i,
                                    (float) dataset.getAggregateColumnValue(tabella, schema.getColumn(i),
                                            QUERY_TYPE.MIN),
                                    (float) dataset.getAggregateColumnValue(tabella, schema.getColumn(i),
                                            QUERY_TYPE.MAX)));
                        } catch (NoValueException e) {
                            // non sono stati trovati il max e il min perchè il resultset è vuoto
                            throw new SQLException();

                            // System.err.println("Non sono presenti valori nella colonna: "+
                            // schema.getColumn(i).getColumnName()+ "\n per questo attributo continuo sono
                            // stati impostati come valore massimo e minimo 0.");
                            // attributeSet.add(new ContinuousAttribute(schema.getColumn(i).getColumnName(),
                            // i, 0, 0));
                        }

                    }
                }
            }
        } catch (SQLException sqlE) {
            // System.err.println("Si è verificato un errore durante la lettura del
            // contenuto della tabella.\n|--->Specifiche dell'errore:"+ sqlE);
            data = new ArrayList<>();
            attributeSet = new LinkedList<>();
            numberOfExamples = 0;
            throw new EmptySetException();
        } finally {
            // viene chiusa la connesione al database
            database.closeConnection();

        }
    }

    /**
     * Restituisce lo stato dell'attributo numberOfExamples (cioè il numero di
     * transazioni presenti nella tabella)
     * 
     * @return numero di transazioni
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce la cardinalità dell'insieme degli attributi
     * 
     * @return il numero di attributi delle tuple
     */
    public int getNumberOfExplanatoryAttributes() {
        return attributeSet.size();
    }

    /**
     * Restituisce lo schema dei dati sotto forma di array
     * 
     * @return array contenente gli attributi in ciascuna tupla
     */
    Attribute[] getAttributeSchema() {
        Attribute copia[] = (Attribute[]) attributeSet.toArray();
        return copia;
    }

    /**
     * Restituisce l'attributo in posizione passata
     * 
     * @param index l'indice associato all'attributo
     * @return attributo presente nell'array degli attributi in posizione indicata
     */
    Attribute getAttribute(int index) {
        return attributeSet.get(index);
    }

    /**
     * Restituisce il valore della matrice in posizione indicata dai parametri
     * 
     * @param exampleIndex   il numero di riga (il numero della tupla)
     * @param attributeIndex il numero di colonna (il numero associato all'attributo
     *                       in base all'ordine degli attributi)
     * @return l'elemento presente nella matrice in posizione indicata dai parametri
     */
    public Object getValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * Restituisce una transazione della tabella (intesa come una tupla)
     * 
     * @param index il numero di riga della transazione
     * @return tupla rappresentante la transazione di riga index
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());
        int i = 0;
        for (Attribute attributo : attributeSet) {
            if (attributo instanceof DiscreteAttribute) {
                tuple.add(new DiscreteItem((DiscreteAttribute) attributo, (String) getValue(index, i)), i);
            } else if (attributo instanceof ContinuousAttribute) {
                tuple.add(new ContinuousItem((ContinuousAttribute) attributo, (Double) getValue(index, i)), i);
            }
            i++;
        }
        return tuple;
    }

    /**
     * Restituisce una stringa in cui è memorizzato lo schema della tabella e le
     * transazioni in essa memorizzate (opportunamente enumerate)
     * 
     * @return la stringa contenente lo stato di data
     */
    @Override
    public String toString() {
        String tabella = new String();
        int i, j;
        for (Attribute attributo : attributeSet) {
            tabella += attributo.getName();
            if (attributo.getIndex() != attributeSet.size() - 1)
                tabella += ",";
        }
        if (getNumberOfExamples() >= 1) {
            for (i = 0; i < getNumberOfExamples(); i++) {
                tabella += "\n" + (i + 1) + ":";
                for (j = 0; j < getNumberOfExplanatoryAttributes() - 1; j++) {
                    tabella += getValue(i, j) + ",";
                }
                tabella += getValue(i, j);
            }
            tabella += "\n";
        }
        return tabella;
    }

}
