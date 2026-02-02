package Graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;

/**
 * Classe che modella il gestore responsabile dell'interfaccia grafica da
 * mostrare all'utente finale
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class GraphicsManager extends JFrame {
    /**
     * Panel principale che contiene tutti gli elementi dell'interfaccia grafica
     */
    private JPanel pagina_principale;

    /**
     * Sezione dell'interfaccia che contiene la barra di caricamento della
     * connessione tra il Client e il server
     */
    private JPanel sezione_caricamento;

    /**
     * Label che contiene una stringa rappresentante lo stato di caricamento della
     * connessione tra il client e il server (come "loading..." o "Successfully
     * connected")
     */
    private JLabel scritta_loading;

    /**
     * Panel che rappresenta il form (da "compilare") con cui l'utente finale può
     * comunicare con il server (questo contiene bottoni, campi di testo, ecc.)
     */
    private JPanel form_scelte;

    /**
     * Colore dell'intero form_scelte
     */
    private Color background_color;

    /**
     * Bottone per selezionare l'opzione di caricamento dei cluster da file
     */
    private JButton bottone_file;

    /**
     * Bottone per selezionare l'opzione di lettura dei dati dal database ed
     * esecuzione dell'algoritmo QT
     */
    private JButton bottone_db;

    /**
     * Campo di testo per l'inserimento del nome della tabella da leggere dal
     * database (o del nome della tabella su cui è stato già eseguito l'algoritmo QT
     * e di cui si vuol recuperare il file di salvataggio)
     */
    private JTextField nome_tabella;

    /**
     * Campo di testo per l'inserimento del raggio con cui eseguire l'algoritmo QT
     * (o raggio con cui è stato lanciato l'algoritmo QT sulla tabella, il cui nome
     * è presente nel campo nome_tabella, di cui si vuol recuperare il file di
     * salvataggio)
     */
    private JTextField raggio;

    /**
     * Bottone di conferma che invia il contenuto del form al server (il quale
     * provvede a caricare il file di salvataggio del ClusterSet o a lanciare
     * l'algoritmo QT, sulla base della scelta espressa nel suddetto form)
     */
    private JButton bottone_invio;

    /**
     * Oggetto CountDownLatch utilizzato per la sincronizzazione tra thread,
     * consente di bloccare la sezione del form in cui viene richiesto il nome della
     * tabella e raggio fino a quando non viene selezionata un'operazione tra
     * "caricare il file" e "eseguire l'algoritmo QT leggendo la tabella dal DB"
     */
    private CountDownLatch latch;

    /**
     * Array contenente i risultati del form compilato dall'utente:
     * <ul>
     * <li>[0] = scelta dell'operazione (1 che indica il caricamento del file o 2
     * che indica la lettura della tabella dal database)</li>
     * <li>[1] = nome della tabella</li>
     * <li>[2] = valore del raggio</li>
     * </ul>
     */
    private String[] risultati_form;

    /**
     * Costruttore della Classe GraphicsManager che definisce l'intero layout
     * dell'interfaccia grafica, nascondendo la sezione in cui si può inserire il
     * nome della tabella e raggio, che sono accessibili solo dopo che l'utente ha
     * effettuato una scelta tra le due disponibili.
     * 
     * @param nome Etichetta che deve avere la finestra di comunicazione con
     *             l'utente
     */
    public GraphicsManager(String nome) {
        // nome = "QTClient"
        // crea il frame e lo rende visibile
        super(nome);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 430);
        setLocationRelativeTo(null);
        setResizable(false);
        background_color = new Color(11845315);
        risultati_form = new String[3];

        // pagina principales
        pagina_principale = new JPanel();
        pagina_principale.setLayout(new BoxLayout(pagina_principale, BoxLayout.Y_AXIS));

        // PER IL TITOLO
        JLabel titolo = new JLabel("Client for Clustering");
        titolo.setBorder(new EmptyBorder(15, 0, 10, 0));
        titolo.setFont(new Font("Arial", Font.BOLD, 24)); // font grande e in grassetto
        titolo.setForeground(Color.BLUE); // colore testo
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        pagina_principale.add(titolo);
        // PER IL TITOLO

        // PER LA SEZIONE DI CARICAMENTO
        sezione_caricamento = new JPanel();
        sezione_caricamento.setAlignmentX(Component.CENTER_ALIGNMENT);
        scritta_loading = new JLabel("Loading....");
        sezione_caricamento.add(scritta_loading);
        sezione_caricamento.setBackground(Color.green);
        sezione_caricamento.setMaximumSize(new Dimension(280, 30));
        sezione_caricamento.setVisible(false);
        pagina_principale.add(sezione_caricamento);
        // PER LA SEZIONE DI CARICAMENTO

        // PER IL FORM DELLE SCELTE (Caricare file o db)
        form_scelte = new JPanel();
        form_scelte.setLayout(new BoxLayout(form_scelte, BoxLayout.Y_AXIS));
        form_scelte.setAlignmentX(Component.CENTER_ALIGNMENT);
        form_scelte.setBackground(background_color);
        form_scelte.setMaximumSize(new Dimension(400, 310));
        JPanel riga_scelta = new JPanel();
        riga_scelta.setLayout(new FlowLayout());
        JLabel scegli = new JLabel("Choose one of the following options:");
        scegli.setBorder(new EmptyBorder(20, 0, 5, 0));
        riga_scelta.setMaximumSize(new Dimension(500, 50));
        riga_scelta.setBackground(background_color);
        riga_scelta.add(scegli);
        form_scelte.add(riga_scelta);

        bottone_file = new JButton("Load clusters from file");
        bottone_db = new JButton("Load data from the db");
        JPanel riga_bottone1 = new JPanel();
        riga_bottone1.setMaximumSize(new Dimension(500, 40));
        riga_bottone1.setBackground(background_color);
        riga_bottone1.setLayout(new FlowLayout());
        riga_bottone1.add(new JLabel("1)"));
        bottone_file.setBackground(Color.green);
        riga_bottone1.add(bottone_file);
        JPanel riga_bottone2 = new JPanel();
        riga_bottone2.setMaximumSize(new Dimension(500, 40));
        riga_bottone2.setBackground(background_color);
        riga_bottone2.setLayout(new FlowLayout());
        riga_bottone2.add(new JLabel("2)"));
        bottone_db.setBackground(Color.GREEN);
        riga_bottone2.add(bottone_db);
        form_scelte.add(riga_bottone1);
        form_scelte.add(riga_bottone2);

        pagina_principale.add(form_scelte);
        // PER IL FORM DELLE SCELTE (Caricare file o db)

        // BOTTONI DEL FORM
        JLabel file_or_db = new JLabel();
        JPanel sezione_scelta = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        sezione_scelta.setBorder(new EmptyBorder(0, 50, 0, 10));
        sezione_scelta.setBackground(background_color);
        sezione_scelta.add(file_or_db);

        // -----//
        form_scelte.add(sezione_scelta);
        JPanel contenitore_tabella = new JPanel();
        contenitore_tabella.setLayout(new BoxLayout(contenitore_tabella, BoxLayout.Y_AXIS));
        contenitore_tabella.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenitore_tabella.setBackground(background_color);
        JPanel tabella_input = new JPanel(new GridLayout(2, 2, 6, 6));
        tabella_input.setBorder(new EmptyBorder(0, 20, 0, 0));
        tabella_input.setBackground(background_color);
        tabella_input.add(new JLabel("Table Name:"));
        nome_tabella = new JTextField(12);
        tabella_input.add(nome_tabella);
        tabella_input.add(new JLabel("Radius:"));
        raggio = new JTextField(6);
        tabella_input.add(raggio);
        contenitore_tabella.add(tabella_input);
        bottone_invio = new JButton("Submit");
        JPanel riga_bottone_inivio = new JPanel();
        riga_bottone_inivio.setBackground(background_color);
        riga_bottone_inivio.setLayout(new BoxLayout(riga_bottone_inivio, BoxLayout.X_AXIS));
        riga_bottone_inivio.setAlignmentX(CENTER_ALIGNMENT);
        riga_bottone_inivio.add(bottone_invio);
        riga_bottone_inivio.setBorder(new EmptyBorder(20, 10, 0, 0));
        contenitore_tabella.add(riga_bottone_inivio);

        sezione_scelta.add(contenitore_tabella);
        sezione_scelta.setVisible(false);
        // -----//

        // inner class locali che non necessitano di javadoc
        bottone_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file_or_db.setText(
                        "<html><div style='font-family:sans-serif; font-size:12pt; font-weight:normal;'>You have chosen to load a ClusterSet from a file.<br> Fill in the following fields:</div><html>");
                risultati_form[0] = "1";
                sezione_scelta.setVisible(true);
            }
        });
        bottone_db.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file_or_db.setText(
                        "<html><div style='font-family:sans-serif; font-size:12pt; font-weight:normal;'>You have chosen to read the dataset from DB and run the QT<br> algorithm on it, now fill in the following fields:<div><html>");
                risultati_form[0] = "2";
                sezione_scelta.setVisible(true);
            }
        });
        bottone_invio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!nome_tabella.getText().isEmpty() && !raggio.getText().isEmpty()) {
                        if (Double.parseDouble(raggio.getText()) > 0) {
                            risultati_form[1] = nome_tabella.getText();
                            risultati_form[2] = raggio.getText();

                            if (latch != null) {
                                latch.countDown(); // sblocca GetResultForm
                            }
                        } else {
                            ShowErrorMessage("The entered radius is not a positive value");
                            raggio.setText("");
                        }
                    } else {
                        ShowErrorMessage("Both fields must be entered");
                    }
                } catch (NumberFormatException non_numero) {
                    ShowErrorMessage("The entered radius is not a numerical value");
                    raggio.setText("");
                }

            }
        });
        // BOTTONI DEL FORM

        form_scelte.setVisible(false);
        add(pagina_principale);
        setVisible(true);
    }

    /**
     * Chiude il frame (cioè l'intera pagina di dialogo con l'utente)richiamando il
     * metodo <b>dispose()</b> (che chiude la "Pagina principale" contenente
     * l'intera interfaccia grafica)
     */
    public void closeFrame() {
        dispose();
    }

    /**
     * Metodo che definisce una nuova finestra di interazione con l'utente per
     * ottenere l'indirizzo IP e la Porta con cui instaurare il canale di
     * comunicazione con il server. La finestra che viene aperta è "bloccante", nel
     * senso che non è possibile interagire con la pagina principale fino a quando
     * questa non viene chiusa o non vengono compilati correttamente i campi e viene
     * premuto il bottone di invio
     * 
     * @return Vettore contente:
     *         <ul>
     *         <li>[0] = indirizzo ip inserito dall'utente(esempio:
     *         "localhost")</li>
     *         <li>[1] = Porta inserita dall'utente(esempio: 8080)</li>
     *         </ul>
     */
    public String[] AskForIpAndPort() {
        InitializationGraphics input = new InitializationGraphics(this, "Initialization");
        return input.getIpAndPort();
    }

    /**
     * Apre una nuova finestra in cui viene mostrato all'utente il messaggio di
     * errore passato come parametro. La finestra che viene aperta è "bloccante",
     * nel senso che non è possibile interagire con la pagina principale fino a
     * quando questa non viene chiusa
     * 
     * @param message messaggio di errore da mostrare all'utente
     */
    public void ShowErrorMessage(String message) {
        ErrorGraphics errore = new ErrorGraphics(this, message);
    }

    /**
     * Consente di attivare e disattivare (cioè mostrare, o meno, all'interno
     * dell'interfaccia) la barra di caricamento della connessione con il server
     * 
     * @param flag valore booleano rappresentante lo stato in cui si vuol portare la
     *             barra di caricamento
     */
    public void ShowLoading(Boolean flag) {
        sezione_caricamento.setVisible(flag);
    }

    /**
     * Consente di mostrare all'utente l'avvenuta connessione con il server da parte
     * del client. Dopo aver mostrato l'avvenuta connessione, provvede ad oscurare
     * l'intera barra di caricamento e ad attivare la sezione contenente i campi di
     * input con cui l'utente può scegliere una opzione tra "caricare il file" o
     * "leggere dal DB"
     */
    public void ShowConnected() {

        javax.swing.Timer timer = new javax.swing.Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scritta_loading.setText("Successfully connected");
                sezione_caricamento.setVisible(true);
                javax.swing.Timer timer2 = new javax.swing.Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        scritta_loading.setVisible(false);
                        sezione_caricamento.setVisible(false);
                        form_scelte.setVisible(true);
                    }

                });
                timer2.setRepeats(false);
                timer2.start();
            }
        });
        timer.setRepeats(false);
        timer.start();

    }

    /**
     * Restituisce, dopo che l'utente ha premuto il bottone di invio, il vettore
     * <b>risultati_form</b> contenente i campi compilati nel form dall'utente:
     * <ul>
     * <li>[0] = scelta dell'operazione (1 che indica il caricamento del file o 2
     * che indica la lettura della tabella dal database)</li>
     * <li>[1] = nome della tabella</li>
     * <li>[2] = valore del raggio</li>
     * </ul>
     * 
     * @return il vettore contenente i risultati del form compilato dall'utente
     */
    public String[] GetResultForm() {

        latch = new CountDownLatch(1);

        try {
            latch.await(); // aspetta finché l’utente non preme Submit
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return risultati_form;
    }

    /**
     * Crea una nuova finestra in cui viene mostrato il risultato dell'operazione
     * richiesta dall'utente (cioè i cluster set letti dal file o i clusterset
     * ottenuti dall'algoritmo QT). La finestra che viene aperta è "bloccante",
     * nel senso che non è possibile interagire con la pagina principale fino a
     * quando questa non viene chiusa o non viene premuto il bottone "Ok".
     * 
     * @param risultato Stringa contenente il risultato dell'operazione che si vuol
     *                  mostrare all'utente
     */
    public void ShowResult(String risultato) {
        ShowResult mostrare_risultato = new ShowResult(this, risultato);
        CleanTableRadiusField();
    }

    /**
     * Pulisce i campi <b>nome_tabella</b> e <b>raggio</b> per consentire un nuovo
     * inserimento da parte dell'utente
     */
    public void CleanTableRadiusField() {
        nome_tabella.setText("");
        raggio.setText("");
    }

}
