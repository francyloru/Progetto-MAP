package data;

import java.io.Serializable;
import java.util.*;

/**
 * Classe che modella una tupla come sequenza di coppie Attributo-Valore
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class Tuple implements Serializable {
    /**
     * Array contenente una tupla (intesa come coppie di Attributo-Valore)
     */
    private Item tuple[];

    /**
     * Costruisce la classe Tupla
     * 
     * @param size la lunghezza massima del vettore contenente le coppie
     *             Attributo-Valore
     */
    public Tuple(int size) {
        tuple = new Item[size];
    }

    /**
     * Restituisce la lunghezza del vettore rappresentante la tupla (cioè il numero
     * di coppie Attributo-Valore)
     * 
     * @return il numero di campi della tupla
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'Item nella posizione passata come parametro
     * 
     * @param i posizione dell'Item nella tupla
     * @return l'item (coppia Attributo-Valore) presente in posizione i
     */
    public Item get(int i) {
        return tuple[i];
    }

    /**
     * Inserisce un item (coppia Attributo-Valore) nella tupla in posizione indicata
     * 
     * @param c item da inserire nella posizione indicata
     * @param i la posizione in cui inserire l'item
     */
    public void add(Item c, int i) {
        tuple[i] = c;
    }

    /**
     * Restituisce la distanza tra la tupla passata come parametro e tra la tupla
     * corrente (quella riferita dal this)
     * 
     * @param obj tupla dalla quale calcolare la distanza
     * @return la distanza tra la tupla corrente e quella passata
     */
    public double getDistance(Tuple obj) {
        double distanza = 0.0;
        for (int i = 0; i < getLength(); i++) {
            distanza += this.tuple[i].distance(obj.get(i).getValue());
        }
        return distanza;
    }

    /**
     * Restituisce la media delle distanze tra la tupla corrente e quelle ottenibili
     * dalle righe della matrice in data aventi indice in clusteredData.
     * 
     * @param data          insieme delle transazioni
     * @param clusteredData insieme degli indici delle transazioni (tuple) dalle
     *                      quali calcolare la distanza media
     * @return la distanzaa media della tupla corrente da quelle di
     *         <strong>data</strong>
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
        double p = 0.0, sumD = 0.0;
        // Iterator it = clusteredData.iterator();
        for (Integer i : clusteredData) {
            double d = getDistance(data.getItemSet(i));
            sumD += d;
        }
        p = sumD / clusteredData.size();
        return p;
    }

    /**
     * Restituisce una stringa in cui è memorizzato il contenuto della tupla
     * 
     * @return stringa contenente lo stato di <strong>tuple</strong>
     */
    @Override
    public String toString() {
        String tupla = new String();
        for (int i = 0; i < getLength(); i++) {
            tupla += tuple[i].toString();
        }
        return tupla;
    }
}
