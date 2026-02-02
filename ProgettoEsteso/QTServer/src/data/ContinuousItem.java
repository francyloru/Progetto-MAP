package data;

/**
 * Classe concreta che modella l'item continuo
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
class ContinuousItem extends Item {

    /**
     * Costruisce la classe ContinuousItem
     * 
     * @param attribute attributo continuo da inserire nel campo attribute
     * @param value     valore da inserire nel campo value
     */
    ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra l'elemento passato e quello di istanza
     * 
     * @param a elemento da cui calcolare la distanza
     * @return distanza calcolata tra i due Item
     */
    double distance(Object a) {
        Double i = ((ContinuousAttribute) this.getAttribute()).getScaledValue((double) (this.getValue()));
        Double j = ((ContinuousAttribute) this.getAttribute()).getScaledValue((double) (a));

        return Math.abs(i - j);
    }
}
