package Exceptions;

/**
 * Classe che modella l'eccezione da espellere se l'algoritmo QT restituisce un
 * solo cluster
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class ClusteringRadiusException extends Exception {

    /**
     * Costruisce la classe ClusteringRadiusException (eccezione da espellere se
     * l'algoritmo QT restituisce un solo cluster)
     */
    public ClusteringRadiusException() {
        super("L'algoritmo QT restituisce un solo cluster");
    }
}
