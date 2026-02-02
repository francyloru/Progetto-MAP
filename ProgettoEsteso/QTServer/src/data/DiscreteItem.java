package data;

/**
 * Classe concreta che modella l'item discreto
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
class DiscreteItem extends Item {

    /**
     * Costruisce la classe DiscreteItem
     * 
     * @param attribute attributo discreto da inserire nel campo attribute
     * @param value     valore da inserire nel campo value
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra l'elemento passato e quello di istanza
     * 
     * @param a elemento da cui calcolare la distanza
     * @return valore booleano che assume true se il contenuto di value è uguale
     *         all'elemento passato
     */
    double distance(Object a) {
        if (getValue().equals(a)) {
            return 0;
        } else {
            return 1;
        }
    }
}