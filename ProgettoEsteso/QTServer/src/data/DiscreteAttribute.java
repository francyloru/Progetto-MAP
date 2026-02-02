package data;

import java.util.*;

/**
 * Classe che modella l'attributo di tipo discreto (estende la calsse Attribute)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
class DiscreteAttribute extends Attribute implements Iterable<String> {

    /**
     * Insieme contenente i valori del dominio discreto
     */
    private TreeSet<String> values;

    /**
     * Costruisce la classe DiscreteAttribute
     * 
     * @param name   il nome da inserire nel campo name
     * @param index  identificativo numerico dell'attributo
     * @param values array contenente valori discreti (Stringhe)
     */
    DiscreteAttribute(String name, int index, String values[]) {
        super(name, index);
        this.values = new TreeSet<>();
        for (int i = 0; i < values.length; i++)
            this.values.add(values[i]);
    }

    /**
     * Restituisce un iterator che referenzia i singoli elementi del
     * dominio discreto
     * 
     * @return un iterator ai singoli elementi del dominio discreto
     */
    public Iterator<String> iterator() {
        return values.iterator();
    }

    /**
     * Restituisce il numero di elementi del dominio discreto
     * 
     * @return numero di elementi del dominio discreto
     */
    int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Restituisce lo stato dell'attributo discreto
     * 
     * @return La stringa contenente lo stato dell'attributo continuo
     */
    @Override
    public String toString() {
        String contenuto = new String(super.toString() + ":\n");
        for (String attributo : values) {
            contenuto = contenuto + " " + attributo + " \n";
        }
        return contenuto;
    }
}
