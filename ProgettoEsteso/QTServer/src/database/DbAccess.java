package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce la connesione al database (ne consente l'apertura e la
 * chiusura della connessione)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class DbAccess {

    /**
     * Nome della classe driver JDBC utilizzata per la connessione al database MySQL
     */
    private String DRIVER_CLASS_NAME;

    /**
     * Tipo di Database Management System utilizzato
     */
    private final String DBMS;

    /**
     * Indirizzo del server database (rappresenta l'host su cui è in esecuzione il
     * server MySQL)
     */
    private final String SERVER;

    /**
     * Nome del database da utilizzare
     */
    private final String DATABASE;

    /**
     * Contiene l’identificativo del server su cui risiede la base di dati
     */
    private final String PORT;

    /**
     * Contiene il nome dell’utente per l’accesso alla base di dati
     */
    private final String USER_ID;

    /**
     * Contiene la password di autenticazione per l’utente identificato da USER_ID
     */
    private final String PASSWORD;

    /**
     * Rappresenta la connessione al database
     */
    private Connection conn;

    /**
     * Costruttore della classe DbAccess che inizializza i dati necessari per creare
     * l'URL di collegamento con il database
     */
    public DbAccess() {
        DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
        DBMS = "jdbc:mysql";
        SERVER = "localhost";
        DATABASE = "MapDB";
        PORT = "3306";
        USER_ID = "MapUser";
        PASSWORD = "map";
    }

    /**
     * Inizializza la connessione con il database "MapDB"
     */
    public void initConnection() throws DatabaseConnectionException {
        String connessione = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user=" + USER_ID
                + "&password=" + PASSWORD + "&serverTimezone=UTC";
        try {
            // carica il driver per il database a cui si vuole accedere
            Class.forName(DRIVER_CLASS_NAME);

            // creare la connessione
            conn = DriverManager.getConnection(connessione);

        } catch (LinkageError link) {
            // System.err.println("Si è verificato un errore durante la connessione al
            // database.\n|-->Specifiche dell'errore:"+ link);
            throw new DatabaseConnectionException();
        } catch (ClassNotFoundException classe) {
            // System.out.println("Si è verificato un errore durante la connessione al
            // database.\n|-->Specifiche dell'errore:"+ classe);
            throw new DatabaseConnectionException();
        } catch (SQLException sql) {
            // System.err.println("Si è verificato un errore durante la connessione al
            // database.\n|-->Specifiche dell'errore:" + sql);
            throw new DatabaseConnectionException();
        }

    }

    /**
     * Restituisce la Connessione al database
     * 
     * @return la Connessione al database
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione con il database
     */
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException sql) {
            System.err.println("  |-> Something went wrong while closing the database connection");
        }catch (NullPointerException n) {
            System.err.println("  |-> Something went wrong while closing the database connection");
        }

    }

    /**
     * Restituisce lo stato della connessione (intesa come l'URL usato
     * per aprire la stessa)
     * 
     * @return Stringa contenente l'URL
     */
    public String toString() {
        String url = new String(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user=" + USER_ID
                + "&password=" + PASSWORD + "&serverTimezone=UTC");
        return url;
    }
}
