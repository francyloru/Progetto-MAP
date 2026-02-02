package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un generico Item (cioè una coppia
 * Attributo-Valore)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
abstract class Item implements Serializable {
    /**
     * Attributo contenuto nell'item
     */
    private Attribute attribute;

    /**
     * Valore contenuto nell'item
     */
    private Object value;

    /**
     * Costruisce la classe Item
     * 
     * @param attribute Attributo da inserire nel campo attribute
     * @param value     Valore da inserire nel campo attribute
     */
    Item(Attribute attribute, Object value) {
        this.value = value;
        this.attribute = attribute;
    }

    /**
     * Restituisce l'attributo dell'item
     * 
     * @return contenuto del campo attribute
     */
    Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce il valore dell'item
     * 
     * @return contenuto del campo value
     */
    Object getValue() {
        return value;
    }

    /**
     * Restituisce una stringa in cui è memorizzato il valore contenuto nell'item
     * 
     * @return stringa contenente lo stato di value
     */
    @Override
    public String toString() {
        String valore = value.toString();
        return valore;
    }

    abstract double distance(Object a);
}
