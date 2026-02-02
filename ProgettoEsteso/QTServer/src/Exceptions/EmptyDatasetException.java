package Exceptions;

/**
 * Classe che modella l'eccezione da espellere se il dataset su cui viene
 * eseguito l'algoritmo QT è vuoto
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class EmptyDatasetException extends Exception {

    /**
     * Costruisce la classe EmptyDatasetException (che modella l'eccezione da
     * espellere se il dataset su cui viene eseguito l'algoritmo QT è vuoto)
     */
    public EmptyDatasetException() {
        super("Il dataset è vuoto");
    }
}
