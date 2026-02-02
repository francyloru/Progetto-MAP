package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Exceptions.*;

import data.Data;

import database.DatabaseConnectionException;
import database.EmptySetException;

import mining.QTMiner;

/**
 * Classe che modella il Thread dedicato alle richieste di un singolo client
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class ServerOneClient extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private QTMiner kmeans;

    /**
     * Costruttore della classe ServerOneClient che inizializza i canali di
     * comunicazione con il client sul socket che viene passato come parametro.
     * Come risultato viene creato un Thread dedicato pronto ad esaudire le
     * richieste del client.
     * 
     * @param s socket creato appositamente per un client specifico
     * @throws IOException se si verifica un errore durante a comunicazione tra
     *                     client e server
     */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        kmeans = null;
        start();
    }

    /**
     * Gestisce la comunicazione tra client e server fino a quando i due non la
     * interrompono (per cause 'naturali' o per via di errori di comunicazione).
     * Riceve le richieste dal client e le esaudisce se possibile, altrimenti passa
     * lo specifico messaggio di errore.
     */
    @Override
    public void run() {
        System.out.println("|\n°|-> Dedicated socket successfully created: " + socket);
        String messaggio_ricevuto;
        Data tabella;
        double input;
        int n_cluster;
        double raggio;
        char ripetere_menu;

        try {
            do {
                int scelta = (int) in.readObject();

                switch (scelta) {
                    case 0:// se dobbiamo leggere dal database la tabella
                        System.out.println("  |-> " + socket + ": Chose to load a table from the database");
                        try {

                            try {
                                messaggio_ricevuto = (String) in.readObject();
                                tabella = new Data((String) messaggio_ricevuto);
                                out.writeObject("OK");
                                System.out.println("  |-> " + socket + ": The table '" + messaggio_ricevuto
                                        + "' has been successfully loaded");
                            } catch (DatabaseConnectionException d) {
                                System.err
                                        .println("   |-> " + socket + ": A connection error occurred with the DB");
                                throw new IOException();
                            } catch (EmptySetException v) {
                                System.err.println(
                                        "   |-> " + socket + ": Error, the table is empty or does not exist");
                                out.writeObject("The table is empty or does not exist");

                                throw new ServerException("Error, the table is empty or does not exist");
                            }

                            // ----------------------------prendere il raggio in input

                            input = (int) in.readObject();
                            if (input != 1)
                                throw new ServerException("Incorrect synchronized communication message");
                            raggio = (double) in.readObject();
                            if (!Double.isNaN(raggio)) {
                                if (raggio <= 0) {
                                    throw new NegativeRadiusException(
                                            "The radius entered is less than or equal to 0");
                                }
                                // break;
                            } else {
                                throw new IsNotANumberException(
                                        "The value that was passed is not a number");
                            }
                            kmeans = new QTMiner(raggio);
                            n_cluster = kmeans.compute(tabella);
                            // System.out.println("Raggio:" + raggio);
                            out.writeObject("OK");
                            out.writeObject(n_cluster);
                            // System.out.println("Numero di cluster:" + n_cluster);
                            System.out.println(
                                    "  |-> " + socket
                                            + ": The QT algorithm was successfully executed and generated "
                                            + n_cluster + " Clusters");
                            out.writeObject(kmeans.getC().toString(tabella));

                            // Prende il nome del file su cui memorizzare il clusterset
                            input = (int) in.readObject();
                            if (input != 2)
                                throw new ServerException("Incorrect synchronized communication message");

                            kmeans.salva("file_salvataggio\\" + messaggio_ricevuto + "_" + raggio + ".dat");
                            // System.out.println("Cluster salvato in: " + "file_salvataggio\\" +
                            // messaggio_ricevuto+ "_" + raggio + ".dat");
                            System.out.println(
                                    "  |-> " + socket + ": The Clusterset was saved in: file_salvataggio\\"
                                            + messaggio_ricevuto + "_" + raggio + ".dat");
                            out.writeObject("OK");
                            // ripetere = (char) in.readObject();

                        } catch (EmptyDatasetException vuoto) {
                            System.err.println("   |-> " + socket
                                    + ": Error, the dataset on which the QT algorithm was run is empty");
                            out.writeObject("The dataset on which the QT algorithm was run is empty");
                            // ripetere_menu = 'n';

                        } catch (FileNotFoundException file) {
                            System.err.println("   |-> " + socket + ": Error saving the ClusterSet to file");
                            out.writeObject("Problems saving the ClusterSet to file");
                            // ripetere_menu = 'n';

                        } catch (NegativeRadiusException negativo) {
                            out.writeObject(negativo.getMessage());
                        } catch (IsNotANumberException numero) {
                            out.writeObject(numero.getMessage());
                        } catch (ClusteringRadiusException cluster) {
                            System.err.println("   |-> " + socket
                                    + ": Error, the QT algorithm returned only one cluster");
                            out.writeObject("The QT algorithm returned only one cluster");

                        } catch (ServerException server) {
                        } // QUI

                        break;
                    case 3:// se dobbiamo caricare un clusterset da file
                        System.out.println("  |-> " + socket + ": Chose to load a clusterset from a file");

                        messaggio_ricevuto = (String) in.readObject();// riceve il nome della tabella
                        raggio = (double) in.readObject();// riceve il raggio
                        try {
                            kmeans = new QTMiner("file_salvataggio\\" + messaggio_ricevuto + "_" + raggio + ".dat");
                            // System.out.println("caricato il clusterset da " + "file_salvataggio\\"+
                            // messaggio_ricevuto + "_" + raggio + ".dat");
                            out.writeObject("OK");// cluster caricato correttamente
                            System.out.println(
                                    "  |-> " + socket + ": Loaded the cluster set from the file 'file_salvataggio\\"
                                            + messaggio_ricevuto + "_" + raggio + ".dat'");
                            out.writeObject(kmeans.getC().toString());

                        } catch (FileNotFoundException file) {
                            out.writeObject(
                                    "<html>The file was not found, possible causes: <ul><li> The name of the table is incorrect </li><li> The radius is not valid </li><li> The QT algorithm has never been launched on this table using this radius </li><li> There are no files containing ClusterSet</li></ul></html>");
                            System.err.println("   |-> " + socket + ": Tried to read the file 'file_salvataggio\\"
                                    + messaggio_ricevuto
                                    + "_" + raggio + ".dat' but failed");
                        }

                        break;
                    default:
                        // Se ha mandato un numero non valido
                        in.readObject();
                        throw new ServerException("Incorrect synchronized communication message");
                }
                ripetere_menu = (char) in.readObject();
            } while (Character.toLowerCase(ripetere_menu) == 'y');

        } catch (IOException e) {
            System.err.println(
                    " |-> " + socket + ": A communication failure has occurred between the client and the server");
        } catch (ClassNotFoundException e) {
            System.err.println(
                    " |-> " + socket + ": The client sent an object of a class that is not present on the server");
            try {
                out.writeObject("An object has been sent but its class is not present on the server");
            } catch (IOException io) {
                System.err.println("  |-> " + socket
                        + ": Communication error, it is not possible to communicate that the client sent an incorrect data type");
            }
        } catch (ServerException problema_generale) {
            System.out.println(" |-> " + socket + " Has identified the following error: " + problema_generale);
            try {
                out.writeObject(problema_generale.getMessage());
            } catch (IOException io) {
                System.err.println(
                        "  |-> " + socket + ": A communication failure has occurred between the client and the server");
            }
        } finally {
            try {
                if (out != null)
                    out.close();
                if (out != null)
                    in.close();
                if (socket != null)
                    socket.close();
                System.out.println(" |-> " + socket + ": The socket has been successfully closed.");
            } catch (IOException io) {
                System.err.println(
                        " |-> " + socket + ": A communication failure has occurred between the client and the server.");
            }
        }
    }

}
