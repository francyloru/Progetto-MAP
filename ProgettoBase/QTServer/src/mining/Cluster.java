package mining;

import java.io.Serializable;
import java.util.*;
import data.*;

/**
 * Classe che modella un singolo cluster (inteso come un insieme di tuple la cui
 * distanza è al massimo pari al raggio)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
class Cluster implements Iterable<Integer>, Comparable<Object>, Serializable {
    /**
     * Tupla centroide del cluster
     */
    private Tuple centroid;

    /**
     * Insieme contenente gli indici delle tuple di Data che appartengono al cluster
     */
    private HashSet<Integer> clusteredData;

    /**
     * Costruisce la classe Cluster sulla base della tupla passata (questo metodo
     * non salva l'indice della tupla centroide, per questo in seguito va segnato
     * tale indice)
     * 
     * @param centroid la tupla centroide del cluster
     */
    Cluster(Tuple centroid) {
        this.centroid = centroid;
        clusteredData = new HashSet<>(1);

    }

    /**
     * Restituisce la tupla centroide del cluster
     * 
     * @return tupla centroide del cluster
     */
    Tuple getCentroid() {
        return centroid;
    }

    /**
     * Restituisce un valore booleano true se il cluster non conteneva la tupla
     * indicata e ora la contiene (restituisce falso se la conteneva già)
     * 
     * @param id posizione della tupla nel dataset
     * @return valore booleano che dice se la tupla sta cambiando cluster
     */
    boolean addData(int id) {
        boolean i = false;
        if (!clusteredData.contains(id)) {
            i = true;
            clusteredData.add(id);
        }
        return i;
    }

    /**
     * Verifica se una transazione (cioè la tupla riferita dall'indice passato) è
     * clusterizzata (cioè si trova) nell'cluster corrente
     * 
     * @param id posizione della tupla (nel dataset)
     * 
     * @return valore booleano che dice se la tupla è stata già clusterizzata
     */
    boolean contain(int id) {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove dal cluster la tupla indicata
     * 
     * @param id posizione della tupla (nel dataset)
     */
    void removeTuple(int id) {
        clusteredData.remove(id);

    }

    /**
     * Restituisce il numero di tuple appartenenti al cluster
     * 
     * @return il numero di tuple appartenenti al cluster
     */
    int getSize() {
        return clusteredData.size();
    }

    /**
     * Restituisce un iterator che permette di muoversi tra gli indici delle tuple
     * che appartengono al cluster
     * 
     * @return iterator agli indici delle tuple del cluster
     */
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Restituisce +1 se il cluster corrente è più popoloso del
     * cluster passato come parametro (altrimenti -1 se è meno popoloso)
     * 
     * @return valore che dice se il cluster corrente è più o meno popoloso
     */
    public int compareTo(Object T) {
        int risultato = -1;
        if (T instanceof Cluster) {
            if (clusteredData.size() > ((Cluster) T).clusteredData.size()) {
                risultato = 1;
            }
        }
        return risultato;
    }

    /**
     * Restituisce una stringa contenente lo stato del centroide
     * 
     * @return stringa contenente lo stato del centroide
     */
    @Override
    public String toString() {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i);
        str += ")";
        return str;

    }

    /**
     * Restituisce una stringa contenente lo stato dell'intero cluster (inteso come
     * lo stato delle tuple in esso contenute)
     * 
     * @param data il dataset da cui prelevare le tuple appartenenti al cluster
     * @return stringa contenente lo stato dell'intero cluster
     */
    public String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")\nExamples:\n";
        for (Integer it : clusteredData) {
            str += "[";
            for (int j = 0; j < data.getNumberOfExplanatoryAttributes(); j++)
                str += data.getValue(it, j) + " ";
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(it)) + "\n";

        }

        str += "\nAvgDistance=" + getCentroid().avgDistance(data, clusteredData);
        return str;

    }

}
