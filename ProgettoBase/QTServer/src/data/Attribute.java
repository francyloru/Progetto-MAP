package data;

import java.io.Serializable;

/**
 * Classe che modella l'attributo
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
abstract class Attribute implements Serializable {

    /**
     * Nome simbolico dell'attributo
     */
    private String name;

    /**
     * Identificativo numerico dell'attributo
     */
    private int index;

    /**
     * Costruisce la classe Attribute
     * 
     * @param name  il nome da inserire nel campo name
     * @param index identificativo numerico dell'attributo
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome dell'attributo
     * 
     * @return Il nome dell'attributo
     */
    String getName() {
        return name;
    }

    /**
     * Restituisce l'identificativo numerico dell'attributo
     * 
     * @return l'identificativo numerico dell'attributo
     */
    int getIndex() {
        return index;
    }

    /**
     * Restituisce la stringa rappresentante lo stato di name
     * 
     * @return Stringa contenente lo stato di name
     */
    @Override
    public String toString() {
        return name;
    }
}