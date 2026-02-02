package database;

/**
 * Classe che modella l'eccezione da espellere nel caso in cui il resultset non
 * contenga un valore
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class NoValueException extends Exception {
    /**
     * Costruisce la classe NoValueException (che modella l'eccezione da espellere
     * nel caso in cui il resultset non contenga un valore)
     */
    public NoValueException() {
        super("A value is missing in the result set");
    }

    /**
     * Costruisce la classe NoValueException (che modella l'eccezione da espellere
     * nel caso in cui il resultset non contenga un valore) specificando il
     * messaggio da restituire in caso di errore
     * 
     * @param messaggio messaggio d'errore su cui definire l'eccezione
     */
    public NoValueException(String messaggio) {
        super(messaggio);
    }
}
