package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe che rappresenta il Server principale, il quale si occupa di creare
 * l'infrastruttura per gestire le richieste da parte di più client
 * contemporaneamente
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class MultiServer extends Thread {
    /**
     * Porta sulla quale il server si mette in ascolto
     */
    private int PORT = 8080;

    /**
     * Costruttore della classe MultiServer che crea il server (e che chiama a sua
     * volta il metodo che crea il socket volto ad ascoltare le richieste fatte dai
     * client sulla porta passata come parametro)
     * 
     * @param port porta sulla quale il server deve mettersi in ascolto
     */
    public MultiServer(int port) {
        PORT = port;
        start();
    }

    /**
     * Metodo che crea il socket per ascoltare le richieste mandate dai client e che
     * crea un socket dedicato per ciascuna richiesta presa in carico
     */
    @Override
    public void run() {
        try {
            ServerSocket ascoltatore = new ServerSocket(PORT);

            try {
                System.out.println(
                        "The server is running and waiting for requests from clients.\n|---[ The socket set up to listen for requests is: "
                                + ascoltatore + " ]");
                while (true) {
                    Socket socket_dedicato = ascoltatore.accept();
                    try {
                        new ServerOneClient(socket_dedicato);
                    } catch (IOException io_dedicato) {
                        System.err.println("|-> An error occurred while creating the dedicated socket for the client");
                        socket_dedicato.close();
                    }
                }
            } finally {
                ascoltatore.close();
            }

        } catch (IOException io) {
            System.err.println("An error occurred creating the socket to listen for client requests.");
        }
    }

    /**
     * Main che crea il Server che esaudirà le richieste dei client
     * 
     * @param args
     */
    public static void main(String[] args) {
        MultiServer server = new MultiServer(8080);
    }
}
