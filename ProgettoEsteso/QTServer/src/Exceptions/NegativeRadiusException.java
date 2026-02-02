package Exceptions;

/**
 * Classe che modella l'eccezione da espellere se viene inserito un raggio
 * minore o uguale a 0
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class NegativeRadiusException extends RuntimeException {

    /**
     * Costruisce la classe NegativeRadiustException (che modella l'eccezione da
     * espellere se viene inserito un raggio minore o uguale a 0)
     */
    public NegativeRadiusException() {
        super("E' stato inserito un raggio negativo o uguale a 0");
    }

    public NegativeRadiusException(String s) {
        super(s);
    }
}
