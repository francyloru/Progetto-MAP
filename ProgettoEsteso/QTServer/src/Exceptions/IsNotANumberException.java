package Exceptions;

/**
 * Classe che modella l'eccezione da espellere se il client manda al server un
 * valore che non è un numero
 */
public class IsNotANumberException extends Exception {
    /**
     * Costruttore della classe IsNotANumberException (che modella l'eccezione da
     * espellere se il client manda al server un valore non numerico)
     */
    public IsNotANumberException() {
        super();
    }

    /**
     * Costruttore della classe IsNotANumberException (che modella l'eccezione da
     * espellere se il client manda al server un valore non numerico) specificando
     * il messaggio di errore da associare all''eccezione'
     * 
     * @param s messaggio di errore
     */
    public IsNotANumberException(String s) {
        super(s);
    }
}
