package Exceptions;

/**
 * Classe che modella la generica eccezione che si può verificare sul server per
 * cause disparate (ad esempio errore di connessione con il database, nei
 * messaggi di sincronizzazione, ecc)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class ServerException extends Exception {

    /**
     * Costruisce la classe ServerException(che modella l'eccezione da espellere se
     * si verifica un problema generico sul server)
     */
    public ServerException() {
        super("An error has occurred on the server");
    }

    /**
     * Costruisce la classe ServerException (che modella l'eccezione da espellere se
     * si verifica un problema generico sul server) specificando il messaggio di
     * errore da associare all'eccezione
     * 
     * @param Errore messaggio di errore
     */
    public ServerException(String Errore) {
        super(Errore);
    }
}
