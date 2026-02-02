package data;

/**
 * Classe che modella un attributo continuo
 * (estende la classe astratta Attribute)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
class ContinuousAttribute extends Attribute {

    /**
     * Rappresenta l'estremo superiore dell'intervallo di valori
     * (cioè il Dominio) che l'atttributo può realmente assumere nel dataset
     */
    private double max;

    /**
     * Rappresenta l'estremo inferiore dell'intervallo di valori
     * che l'atttributo può realmente assumere nel dataset
     */
    private double min;

    /**
     * Costruisce la classe ContinuousAttribute
     * 
     * @param name  il nome da inserire nel campo name
     * @param index identificativo numerico dell'attributo
     * @param min   l'estremo inferiore del dominio
     * @param max   l'estremo inferiore del dominio
     */
    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Restituisce il valore scalato del parametro passato in input
     * (ha come codominio [0,1])
     * 
     * @param v valore da scalare in [0,1]
     * @return il valore scalato in [0,1]
     */
    double getScaledValue(double v) {
        return (v - min) / (max - min);
    }

    /**
     * Restituisce lo stato dell'attributo continuo
     * 
     * @return La stringa contenente lo stato dell'attributo continuo
     */
    @Override
    public String toString() {
        String contenuto = new String(super.toString() + " " + min + " " + max);
        return contenuto;
    }
}
