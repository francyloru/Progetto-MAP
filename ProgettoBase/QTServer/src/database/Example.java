package database;

import java.util.*;

/**
 * Classe che modella una transazione letta dalla base di dati
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class Example implements Comparable<Example> {

    /**
     * Tupla letta dalla base di dati (cioè sequenza degli elementi di una riga)
     */
    private List<Object> example = new ArrayList<Object>();

    /**
     * Aggiunge l'elemento passato in coda alla tupla
     * 
     * @param o elemento da aggiungere alla coda
     */
    public void add(Object o) {
        example.add(o);
    }

    /**
     * Restituisce l'elemento nella posizione specificata nella tupla
     * 
     * @param i posizione dell'elemento desiderato
     * @return elemento della tupla in posizione i
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Restituisce un valore positivo se gli elementi della tupla corrente sono più
     * grandi degli elementi della tupla passata come parametro
     * 
     * @return valore intero che indica se la tupla corrente è più grande della
     *         tupla passata
     */
    public int compareTo(Example ex) {

        int i = 0;
        for (Object o : ex.example) {
            if (!o.equals(this.example.get(i))) {
                @SuppressWarnings("unchecked") // Siamo sicuri che arrivi un'istanza di classe che ha implementato
                                               // l'interfaccia Comparable<Object> quind possiamo sopprimere senza dubbi
                                               // l'avviso che dà il compilatore (cioè 'unchecked cast')
                Comparable<Object> comp = (Comparable<Object>) o;
                return comp.compareTo(example.get(i));
            }

            i++;
        }
        return 0;
    }

    /**
     * Restituisce una stringa contiene gli elementi della tupla
     * 
     * @return lo stato della tupla
     */
    @Override
    public String toString() {
        String str = "";
        for (Object o : example)
            str += o.toString() + " ";
        return str;
    }

}