package database;

/**
 * Classe che modella l'eccezione da espellere nel caso in cui il resultset sia
 * vuoto
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class EmptySetException extends Exception {

    /**
     * Costruisce la classe EmptySetException (che modella l'eccezione da espellere
     * nel caso in cui il resultset sia vuoto)
     */
    public EmptySetException() {
        super("The resultset is empty");
    }
}
