package database;

/**
 * Classe che modella l'eccezione da espellere se la connessione
 * con il database fallisce
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class DatabaseConnectionException extends Exception {

    /**
     * Costruisce la classe DatabaseConnectionException (che modella l'eccezione da
     * espellere se la connessione con il database fallisce)
     */
    public DatabaseConnectionException() {
        super("The connection with the database failed");
    }

}
