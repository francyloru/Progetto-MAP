package keyboardinput;

import java.io.*;
import java.util.*;

/**
 * Classe che fornisce metodi per leggere diversi tipi di dati dallo standard
 * input.
 * 
 * @author Lewis and Loftus
 */
public class Keyboard {

    /**
     * Flag che controlla se i messaggi di errore vengono stampati sullo standard
     * output.
     * Il valore predefinito è true.
     */
    private static boolean printErrors = true;

    /**
     * Contatore che tiene traccia del numero di errori di input verificatisi.
     * Inizializzato a 0.
     */
    private static int errorCount = 0;

    /**
     * Restituisce il conteggio attuale degli errori.
     *
     * @return Il numero di errori verificatisi durante le operazioni di input
     */
    public static int getErrorCount() {
        return errorCount;
    }

    /**
     * Reimposta il conteggio degli errori corrente a zero.
     *
     * @param count Parametro non utilizzato (sembra un errore nel codice originale)
     */
    public static void resetErrorCount(int count) {
        errorCount = 0;
    }

    /**
     * Indica se gli errori di input vengono attualmente stampati sullo standard
     * output.
     *
     * @return true se gli errori vengono stampati, false altrimenti
     */
    public static boolean getPrintErrors() {
        return printErrors;
    }

    /**
     * Imposta il flag che indica se gli errori di input devono essere
     * stampati sullo standard output.
     *
     * @param flag true per stampare gli errori, false altrimenti
     */
    public static void setPrintErrors(boolean flag) {
        printErrors = flag;
    }

    /**
     * Incrementa il conteggio degli errori e stampa il messaggio di errore se
     * appropriato.
     *
     * @param str Il messaggio di errore da stampare
     */
    private static void error(String str) {
        errorCount++;
        if (printErrors)
            System.out.println(str);
    }

    // ************* Tokenized Input Stream Section ******************

    /**
     * Memorizza temporaneamente un token già letto ma non ancora elaborato.
     * Utilizzato principalmente dal metodo readChar quando si legge una stringa
     * con più di un carattere.
     */
    private static String current_token = null;

    /**
     * Tokenizer che suddivide l'input in token basandosi su delimitatori
     * come spazi e ritorni a capo.
     */
    private static StringTokenizer reader;

    /**
     * Utilizza un InputStreamReader per convertire i byte in caratteri.
     */
    private static BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in));

    /**
     * Ottiene il prossimo token di input assumendo che possa trovarsi su righe
     * di input successive.
     *
     * @return Il prossimo token disponibile nell'input
     */
    private static String getNextToken() {
        return getNextToken(true);
    }

    /**
     * Ottiene il prossimo token di input, che potrebbe essere già stato letto.
     *
     * @param skip Se true, salta i delimitatori
     * @return Il prossimo token disponibile
     */
    private static String getNextToken(boolean skip) {
        String token;

        if (current_token == null)
            token = getNextInputToken(skip);
        else {
            token = current_token;
            current_token = null;
        }

        return token;
    }

    /**
     * Ottiene il prossimo token dall'input, che può provenire dalla
     * riga di input corrente o da una successiva. Il parametro
     * determina se vengono utilizzate le righe successive.
     *
     * @param skip Se true, salta i delimitatori
     * @return Il prossimo token di input
     */
    private static String getNextInputToken(boolean skip) {
        final String delimiters = " \t\n\r\f";
        String token = null;

        try {
            if (reader == null)
                reader = new StringTokenizer(in.readLine(), delimiters, true);

            while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
                while (!reader.hasMoreTokens())
                    reader = new StringTokenizer(in.readLine(), delimiters,
                            true);

                token = reader.nextToken();
            }
        } catch (Exception exception) {
            token = null;
        }

        return token;
    }

    /**
     * Verifica se non ci sono più token da leggere sulla
     * riga di input corrente.
     *
     * @return true se non ci sono più token, false altrimenti
     */
    public static boolean endOfLine() {
        return !reader.hasMoreTokens();
    }

    // ************* Reading Section *********************************

    /**
     * Legge una stringa completa dallo standard input.
     *
     * @return La stringa letta
     */
    public static String readString() {
        String str;

        try {
            str = getNextToken(false);
            while (!endOfLine()) {
                str = str + getNextToken(false);
            }
        } catch (Exception exception) {
            // error("Error reading String data, null value returned.");
            str = null;
        }
        return str;
    }

    /**
     * Legge una sottostringa delimitata da spazi (una parola) dallo
     * standard input.
     *
     * @return La parola letta
     */
    public static String readWord() {
        String token;
        try {
            token = getNextToken();
        } catch (Exception exception) {
            // error("Error reading String data, null value returned.");
            token = null;
        }
        return token;
    }

    /**
     * Legge un valore booleano dallo standard input.
     * Accetta "true" o "false".
     *
     * @return Il valore booleano letto
     */
    public static boolean readBoolean() {
        String token = getNextToken();
        boolean bool;
        try {
            if (token.toLowerCase().equals("true"))
                bool = true;
            else if (token.toLowerCase().equals("false"))
                bool = false;
            else {
                // error("Error reading boolean data, false value returned.");
                bool = false;
            }
        } catch (Exception exception) {
            // error("Error reading boolean data, false value returned.");
            bool = false;
        }
        return bool;
    }

    /**
     * Legge un carattere dallo standard input.
     *
     * @return Il carattere letto
     */
    public static char readChar() {
        String token = getNextToken(false);
        char value;
        try {
            if (token.length() > 1) {
                current_token = token.substring(1, token.length());
            } else
                current_token = null;
            value = token.charAt(0);
        } catch (Exception exception) {
            // error("Error reading char data, MIN_VALUE value returned.");
            value = Character.MIN_VALUE;
        }

        return value;
    }

    /**
     * Legge un intero dallo standard input.
     *
     * @return Il valore intero letto
     */
    public static int readInt() {
        String token = getNextToken();
        int value;
        try {
            value = Integer.parseInt(token);
        } catch (Exception exception) {
            // error("Error reading int data, MIN_VALUE value returned.");
            value = Integer.MIN_VALUE;
        }
        return value;
    }

    /**
     * Legge un long integer dallo standard input.
     *
     * @return Il valore long letto
     */
    public static long readLong() {
        String token = getNextToken();
        long value;
        try {
            value = Long.parseLong(token);
        } catch (Exception exception) {
            // error("Error reading long data, MIN_VALUE value returned.");
            value = Long.MIN_VALUE;
        }
        return value;
    }

    /**
     * Legge un numero float dallo standard input.
     *
     * @return Il valore float letto
     */
    public static float readFloat() {
        String token = getNextToken();
        float value;
        try {
            // value = (new Float((token))).floatValue();
            value = Float.parseFloat(token);
        } catch (Exception exception) {
            // error("Error reading float data, NaN value returned.");
            value = Float.NaN;
        }
        return value;
    }

    /**
     * Legge un numero double dallo standard input.
     *
     * @return Il valore double letto
     */
    public static double readDouble() {
        String token = getNextToken();
        double value;
        try {
            // value = (new Double(token)).doubleValue();
            value = Double.parseDouble(token);
        } catch (Exception exception) {
            // error("Error reading double data, NaN value returned.");
            value = Double.NaN;
        }
        return value;
    }
}
