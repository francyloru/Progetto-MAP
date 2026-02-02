import java.io.*;
import java.net.*;
import keyboardinput.Keyboard;

/**
 * Classe che consente al Client di interagire con il server per:
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
     * @param args
     */

    /**
     * Stream per gli output (diretti dal client verso il server)
     */
    private ObjectOutputStream out;

    /**
     * Stream per gli input (diretti dal server verso il client)
     */
    private ObjectInputStream in;

    /**
     * Costruttore che stabilisce una connessione con un server (dati come parametro
     * l'indirizzo IP
     * e porta) e inizializza i flussi di input/output per inviare e ricevere
     * oggetti
     * 
     * @param ip   indirizzo ip del server
     * @param port porta sulla quale il server è in ascolto
     * @throws IOException se ci sono problemi di lettura/scrittura nel canale di
     *                     comunicazione
     */
    public MainTest(String ip, int port) throws IOException {
        // comunicazione tra client e server
        try {
            InetAddress addr = InetAddress.getByName(ip); // ip
            try {

                System.out.println("addr = " + addr);
                Socket socket = new Socket(addr, port); // Port
                System.out.println(socket);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                ; // stream con richieste del client
            } catch (ConnectException connessione) {
                throw new IOException("Socket creation failed.");
            }
        } catch (UnknownHostException host) {
            throw new IOException("The host's IP address is not valid.");
        }
        ; // stream con richieste del client
    }

    /**
     * Stampa il menu contenente le due possibili scelte consentite all'utente
     * finale, cioè caricare il cluster da un file già presente sul Server o
     * caricare un Cluster utilizzando una tabella presente nel database
     * 
     * @return numero associato alla scelta dell'utente(1 o 2)
     */
    private int menu() {
        int answer;
        do {
            System.out.println("(1) Load clusters from file");
            System.out.println("(2) Load data from db");
            System.out.print("(1/2):");

            answer = Keyboard.readInt();
        } while (answer <= 0 || answer > 2);
        return answer;

    }

    /**
     * Metodo che consente all'utente di effetuare la richiesta di caricamento di un
     * cluster da un file già presente sul server (per accedere al file è necessario
     * sapere il nome della tabella e il raggio con cui è stato effettuato il
     * clustering)
     * 
     * @return una stringa rappresentante il Cluster letto dal file
     * @throws SocketException        l'operazione fallisce per motivi
     *                                legati al socket
     * @throws ServerException        se non è stato trovato il file contenente il
     *                                cluster con quel dato nome e raggio
     * @throws IOException            se ci sono problemi di lettura/scrittura nel
     *                                canale di comunicazione
     * @throws ClassNotFoundException se la classe dell'oggetto che viene passato al
     *                                client non è presente tra le classi del client
     */
    private String learningFromFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(3);// Passa il numero 3 al server

        System.out.print("Table Name:");
        String tabName = Keyboard.readString();
        out.writeObject(tabName);// Passa il nome della tabella di cui è stato effettuato il clustering
        double r = 1.0;
        do {
            System.out.print("Radius:");
            r = Keyboard.readDouble();
        } while (r <= 0);
        out.writeObject(r);// Passa il raggio del Cluster al Server
        String result = (String) in.readObject();
        if (result.equals("OK"))// Se riceve il messaggio 'OK' dal server vuol dire che ha trovato il cluster
                                // specifico e lo restituisce
            return (String) in.readObject();
        else
            throw new ServerException(result);// ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Metodo che consente all'utente di passare al server il nome della tabella da
     * clusterizzare (questa deve essere presente nel DB)
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
    private void storeTableFromDb() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(0);// Passa il numero 0 al Server
        System.out.print("Table name:");
        String tabName = Keyboard.readString();
        out.writeObject(tabName);// Passa il nome della tabella da cui leggere le tuple
        String result = (String) in.readObject();
        if (!result.equals("OK"))// SE riceve il messaggio 'OK' allora la tabella è stata trovata e le tuple sono
                                 // state caricate
            throw new ServerException(result);// ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Metodo che consente all'utente finale di passare il raggio con cui
     * effetturare il clustering della tabella che è stata già letta dal DB (usando
     * prima il metodo storeTableFromDb)
     * 
     * @return il numero di cluster trovati per quella tabella
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
    private String learningFromDbTable() throws SocketException, ServerException, IOException, ClassNotFoundException {
        out.writeObject(1);// Passa il numero '1' al Server
        double r = 1.0;
        do {
            System.out.print("Radius:");
            r = Keyboard.readDouble();
        } while (r <= 0);
        out.writeObject(r);// Passa il raggio al server
        String result = (String) in.readObject();
        if (result.equals("OK")) {// SE riceve OK allora il raggio va bene e subito dopo riceve il numero di
                                  // cluster e dopo ancora una stringa rappresentate i cluster
            System.out.println("Number of Clusters:" + in.readObject());
            return (String) in.readObject();
        } else
            throw new ServerException(result); // ALTRIMENTI restituisce un ServerExcpetion

    }

    /**
     * Metodo che consente all'utente finale di salvare il clusterset ottenuto
     * dall'algoritmo QT in un file che ha nome nel formato
     * <b>nome-tabella</b>_<b>raggio</b>.<b>dat</b>
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
     * Main che gestisce il dialogo tra l'utente finale e il Server
     * 
     * @param args indirizzo ip e porta associati al Server da interpellare
     */
    public static void main(String[] args) {
        try {

            MainTest main = null;
            String ip = args[0];
            int port;
            try {
                // prima int port = new Integer(args[1]).intValue();
                port = Integer.parseInt(args[1]);
                // int port = 8080;
            } catch (NumberFormatException non_numero) {
                System.err.println("You have not entered a numerical value at the port !!");
                return;
            }

            try {
                main = new MainTest(ip, port);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }

            try {
                do {
                    int menuAnswer = main.menu();
                    char repeat;
                    switch (menuAnswer) {
                        case 1:
                            try {
                                String kmeans = main.learningFromFile();
                                System.out.println(kmeans);
                            } catch (SocketException e) {
                                System.out.println(e);
                                return;
                            } catch (FileNotFoundException e) {
                                System.out.println(e);
                                return;
                            } catch (IOException e) {
                                System.out.println(e);
                                return;
                            } catch (ClassNotFoundException e) {
                                System.out.println(e);
                                return;
                            } catch (ServerException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 2: // learning from db

                            while (true) {
                                try {
                                    main.storeTableFromDb();
                                    break; // esce fuori dal while
                                } catch (SocketException e) {
                                    System.out.println(e);
                                    return;
                                } catch (FileNotFoundException e) {
                                    System.out.println(e);
                                    return;
                                } catch (IOException e) {
                                    System.out.println(e);
                                    return;
                                } catch (ClassNotFoundException e) {
                                    System.out.println(e);
                                    return;
                                } catch (ServerException e) {
                                    System.out.println(e.getMessage());
                                }
                            } // end while [viene fuori dal while con un db (in alternativa il programma
                              // termina)

                            char answer = 'y';// itera per learning al variare di k
                            do {
                                while (true) {
                                    try {
                                        String clusterSet = main.learningFromDbTable();
                                        System.out.println(clusterSet);

                                        main.storeClusterInFile();
                                        break;

                                    } catch (SocketException e) {
                                        System.out.println(e);
                                        return;
                                    } catch (FileNotFoundException e) {
                                        System.out.println(e);
                                        return;
                                    } catch (ClassNotFoundException e) {
                                        System.out.println(e);
                                        return;
                                    } catch (IOException e) {
                                        System.out.println(e);
                                        return;
                                    } catch (ServerException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }

                                do {
                                    System.out.print("Would you repeat?(y/n)");
                                    answer = Keyboard.readChar();

                                } while (Character.toLowerCase(answer) != 'y' && Character.toLowerCase(answer) != 'n');

                                try {// aggiunto per permettere l'iterazione
                                    main.out.writeObject(answer);
                                } catch (IOException io) {
                                    System.err.println(io);
                                    return;
                                }
                            } while (Character.toLowerCase(answer) == 'y');
                            break; // fine case 2
                        default:
                            System.out.println("Invalid option!");
                    }
                    do {
                        System.out.print("would you choose a new operation from menu?(y/n)");
                        repeat = Character.toLowerCase(Keyboard.readChar());

                    } while (Character.toLowerCase(repeat) != 'y' && Character.toLowerCase(repeat) != 'n');

                    try {
                        main.out.writeObject(repeat);
                    } catch (IOException io) {
                        System.err.println("Errore:" + io);
                        return;
                    }

                    if (repeat != 'y') {
                        break;
                    }
                } while (true);

            } finally {

                try {

                    if (main.out != null)
                        main.out.close();
                    if (main.out != null)
                        main.in.close();

                } catch (SocketException socket) {
                    System.err.println(
                            "The communication channels with the server have been closed after a communication issue.");
                } catch (IOException io) {
                    System.err.println(io);
                }
            }
        } catch (IndexOutOfBoundsException indici) {
            System.err
                    .println("Not all the required parameters have been passed, which are:\n  - IP address\n  - Port");
        } catch (IllegalArgumentException negativo) {
            System.err.println("You have entered a negative numerical number !!");
            return;
        }
    }

}
