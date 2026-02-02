import java.io.*;
import java.net.*;

import Graphics.GraphicsManager;

/**
 * Classe che consente al Client di interagire con il server, mediante
 * l'intefaccia grafica, per:
 * <ul>
 * <li>Scoprire nuovi Cluster, leggendo una tabella dal database</li>
 * <li>Recuperare i Cluster salvati sul server</li>
 * </ul>
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class MainTest {

    /**
     * Stream per gli output (diretti dal client verso il server)
     */
    private ObjectOutputStream out;

    /**
     * Stream per gli input (diretti dal server verso il client)
     */
    private ObjectInputStream in;

    /**
     * Componente grafico che consente di mostrare all'utente finale le finestre di
     * dialogo
     */
    static private GraphicsManager grafica;

    /**
     * Costruttore che stabilisce una connessione con il server (sulla base
     * dell'indirizzo IP e della Porta che vengono chiesti all'utente mediante il
     * relativo form e, se la connessione avviene con successo ne viene mostrato lo
     * stato mediante una barra di caricamento) e inizializza i flussi di
     * input/output per inviare e ricevere oggetti dal/verso il server.
     * 
     * @throws IOException se ci sono problemi di lettura/scrittura nel canale di
     *                     comunicazione
     */
    public MainTest() throws IOException {
        // comunicazione tra client e server

        // Crea la pagina principale
        grafica = new GraphicsManager("QTClient");

        String valori[];
        String ip;
        int port;

        valori = grafica.AskForIpAndPort();
        grafica.ShowLoading(true);
        ip = valori[0];

        try {
            port = Integer.parseInt(valori[1]);
            InetAddress addr = InetAddress.getByName(ip);
            try {

                Socket socket = new Socket(addr, port);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                grafica.ShowConnected();

                ; // stream con richieste del client
            } catch (ConnectException connessione) {
                grafica.ShowLoading(false);
                throw new IOException("Socket creation failed.");
            }
        } catch (UnknownHostException host) {
            grafica.ShowLoading(false);
            throw new IOException("The host's IP address is not valid.");
        } catch (NumberFormatException numero) {
            throw new IOException("A non-numeric value was passed as port.");
        }
        ; // stream con richieste del client
    }

    /**
     * Gestisce la comunicazione con il server per ricevere (sotto forma di Stringa)
     * il ClusterSet memorizzato sul server nel file denominato
     * <b>tabella</b>_<b>raggio</b>.dat in cui il nome della tabella e il raggio
     * vengono passati come parametro al metodo.
     * 
     * @param Tabella Nome della tabella di cui recuperare il file di salvataggio
     * @param Raggio  Raggio con cui è stato eseguito l'algoritmo QT sulla tabella
     *                specificata
     * @return una stringa rappresentante il ClusterSet letto dal file
     * 
     * @throws SocketException        l'operazione fallisce per motivi
     *                                legati al socket
     * @throws ServerException        se non è stato trovato il file contenente il
     *                                cluster con quel dato nome e raggio
     * @throws IOException            se ci sono problemi di lettura/scrittura nel
     *                                canale di comunicazione
     * @throws ClassNotFoundException se la classe dell'oggetto che viene passato al
     *                                client non è presente tra le classi del client
     */
    private String learningFromFile(String Tabella, String Raggio)
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(3);// Passa il numero 3 al server
        out.writeObject(Tabella);// Passa il nome della tabella di cui è stato effettuato il clustering
        out.writeObject(Double.parseDouble(Raggio));// Passa il raggio del Cluster al Server
        String result = (String) in.readObject();
        if (result.equals("OK"))// Se riceve il messaggio 'OK' dal server vuol dire che ha trovato il cluster
                                // specifico e lo restituisce
            return (String) in.readObject();
        else
            throw new ServerException(result);// ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Gestisce la comunicazione con il server per passare al server il nome della
     * tabella (passata come parametro al metodo) da clusterizzare (questa deve
     * essere presente nel DataBase)
     * 
     * @param Tabella Nome della tabella da leggere dal DataBase
     * 
     * @throws SocketException        l'operazione fallisce per motivi legati al
     *                                socket
     * @throws ServerException        se non è stata trovata la tabella avente quel
     *                                dato nome
     * @throws IOException            se ci sono problemi di lettura/scrittura nel
     *                                canale di comunicaziones
     * @throws ClassNotFoundException se la classe dell'oggetto che viene passato al
     *                                client non è presente tra le classi del client
     */
    private void storeTableFromDb(String Tabella)
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(0);// Passa il numero 0 al Server

        out.writeObject(Tabella);// Passa il nome della tabella da cui leggere le tuple
        String result = (String) in.readObject();
        if (!result.equals("OK"))// SE riceve il messaggio 'OK' allora la tabella è stata trovata e le tuple sono
                                 // state caricate
            throw new ServerException(result);// ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Gestisce la comunicazione con il server per passare il raggio (passato come
     * parametro al metodo) con cui effetturare il clustering della tabella che è
     * stata già letta dal DataBase (da utilizzare dopo il metodo
     * storeTableFromDb())
     * 
     * @param Raggio Raggio con cui lannciare l'algoritmo QT
     * @return il numero di cluster trovati per quella tabella
     * 
     * @throws SocketException        l'operazione fallisce per motivi legati al
     *                                socket
     * @throws ServerException        se il raggio passato non è valido (ad esempio
     *                                perché causa l'esistenza di un singolo
     *                                cluster)
     * @throws IOException            se ci sono problemi di lettura/scrittura nel
     *                                canale di comunicazione
     * @throws ClassNotFoundException se la classe dell'oggetto che viene passato al
     *                                client non è presente tra le classi del client
     */
    private String learningFromDbTable(String Raggio)
            throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(1);// Passa il numero '1' al Server

        out.writeObject(Double.parseDouble(Raggio));// Passa il raggio al server
        String result = (String) in.readObject();
        if (result.equals("OK")) {// SE riceve OK allora il raggio va bene e subito dopo riceve il numero di
                                  // cluster e dopo ancora una stringa rappresentate i cluster

            // SE SERVE SAPERE IL NUMERO DI CLUSTER
            in.readObject();
            // System.out.println("Number of Clusters:" + in.readObject());
            return (String) in.readObject();
        } else
            throw new ServerException(result); // ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Metodo che consente all'utente finale di salvare il clusterset ottenuto
     * dall'algoritmo QT in un file che ha nome nel formato
     * <b>tabella</b>_<b>raggio</b>.dat
     * 
     * @throws SocketException        l'operazione fallisce per motivi legati al
     *                                socket
     * @throws ServerException        se il nome del file non è valido (ad esempio
     *                                non è nel formato nome.estensione)
     * @throws IOException            se ci sono problemi di lettura/scrittura nel
     *                                canale di comunicazione
     * @throws ClassNotFoundException se la classe dell'oggetto che viene passato al
     *                                client non è presente tra le classi del client
     */
    private void storeClusterInFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(2);// Passa il numero '2' al Server

        String result = (String) in.readObject();
        if (!result.equals("OK"))// SE riceve 'OK' allora il salvataggio dei cluster è avvenuto con successo
            throw new ServerException(result);// ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Main che gestisce il dialogo tra l'utente finale (usufruendo dell'interfaccia
     * grafica) e il Server
     */
    public static void main(String[] args) {

        MainTest main = null;
        try {
            main = new MainTest();
        } catch (IOException e) {
            grafica.ShowErrorMessage(e.getMessage());
            grafica.closeFrame();
            return;
        }

        try {
            do {
                String[] result = grafica.GetResultForm();
                int menuAnswer = Integer.parseInt(result[0]);
                try {
                    switch (menuAnswer) {
                        case 1:// leggere dal file

                            grafica.CleanTableRadiusField();
                            String kmeans = main.learningFromFile(result[1], result[2]);
                            grafica.ShowResult(kmeans);

                            break;
                        case 2: // learning from db

                            grafica.CleanTableRadiusField();
                            main.storeTableFromDb(result[1]);
                            String clusterSet = main.learningFromDbTable(result[2]);
                            grafica.ShowResult(clusterSet);

                            main.storeClusterInFile();
                            // esce fuori dal while

                            break; // fine case 2
                        default:
                            // non si arriva qui ma per sicurezza...
                            grafica.ShowErrorMessage("You have entered an invalid option");
                    }// FINE SWITCH

                } catch (SocketException e) {
                    grafica.ShowErrorMessage(
                            "<html>The connection with the server is dropped, the software<br> will be closed automatically after pressing the -Ok- button.</html>");
                    // grafica.closeFrame();
                    return;
                } catch (FileNotFoundException e) {
                    grafica.ShowErrorMessage(e.getMessage());
                    return;
                } catch (IOException e) {
                    grafica.ShowErrorMessage(e.getMessage());
                    return;
                } catch (ClassNotFoundException e) {
                    grafica.ShowErrorMessage(e.getMessage());
                    return;
                } catch (ServerException e) {
                    grafica.ShowErrorMessage(e.getMessage());

                } catch (ClassCastException e) {
                    grafica.ShowErrorMessage(e.getMessage());
                    return;
                } catch (NumberFormatException e) {
                    grafica.ShowErrorMessage(e.getMessage());
                    return;
                }

                try {
                    main.out.writeObject('y');
                } catch (IOException e) {
                    grafica.ShowErrorMessage(e.getMessage());
                }

            } while (true);

        } finally {

            try {

                if (grafica != null)
                    grafica.closeFrame();
                if (main.out != null)
                    main.out.close();
                if (main.out != null)
                    main.in.close();

            } catch (IOException io) {
                return;
            }
        }

    }

}
