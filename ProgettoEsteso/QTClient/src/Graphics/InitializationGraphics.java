package Graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

/**
 * Classe che modella la finestra di dialogo con l'utente che consente a
 * quest'ultimo di inserire l'indirizzo ip e porta necessari per rintracciare il
 * server sul quale risiede tutta la logica dell'algoritmo QT.
 * La pagina creata si chiuderà solo se i campi di input vengono compilati
 * correttamente, a prescindere dal fatto che la connessione con il server sia
 * avvenuta con successo o meno
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class InitializationGraphics extends JDialog {

    /**
     * Campo di testo in cui l'utente deve inserire l'indirizzo ip del server
     */
    private JTextField ip;

    /**
     * Campo in cui l'utente deve inserire la porta sulla quale il server è in
     * ascolto
     */
    private JTextField port;

    /**
     * Bottone che l'utente deve premere per provare ad instaurare la connessione
     * con il server mediante l'indirizzo ip e la porta che sono stati inseriti nei
     * campi <b>ip</b> e <b>port</b>.
     */
    private JButton bottone;

    /**
     * Vettore contenente le risposte dell'utente nel formato stringa, cioè:
     * <ul>
     * <li>[0] = ip</li>
     * <li>[1] = porta</li>
     * </ul>
     */
    private String[] Result;

    /**
     * Costruttore della classe InitializationGraphics che crea la prima finestra di
     * dialogo con l'utente che consente a quest'ultimo di inserire l'indirizzo ip e
     * la porta sulla quale il server è in ascolto.
     * La pagina viene creata ma non è subito visibile, per essere "attivata" è
     * necessario utilizzare il metodo <b>getIpAndPort()</b> che mette in attesa la
     * pagina chiamante fino a quando non viene premuto il pulsante di invio.
     * 
     * @param parent frame che necessita dell'indirizzo ip e della porta
     * @param title  etichetta da dare alla finestra che viene creata
     */
    public InitializationGraphics(Frame parent, String title) {
        super(parent, title, true); // true = modale
        Result = new String[2];
        setSize(330, 200);
        setResizable(false);
        setLocationRelativeTo(parent);

        // sposta più in basso
        setLocation(getX(), getY() + 20);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JPanel tabella_input = new JPanel(new GridLayout(2, 2, 30, 30));
        tabella_input.add(new JLabel("IP address: "));
        ip = new JTextField(15);
        tabella_input.add(ip);
        tabella_input.add(new JLabel("Port: "));
        port = new JTextField(4);
        tabella_input.add(port);
        tabella_input.setBorder(new EmptyBorder(25, 20, 10, 20));
        form.add(tabella_input);

        JPanel riga_bottone = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottone = new JButton("Submit");
        riga_bottone.add(bottone);
        form.add(riga_bottone);
        add(form);

        // Azione bottone: salva ip e chiudi dialog
        bottone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ip.getText().isEmpty() && !port.getText().isEmpty()) {

                    try {

                        if (Integer.parseInt(port.getText()) >= 0) {

                            Result[0] = ip.getText();
                            Result[1] = port.getText();
                            dispose();
                        } else {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException non_intero) {
                        ErrorGraphics errore = new ErrorGraphics(parent, "The port must be an positive integer number");
                        port.setText("");
                    }
                } else {
                    ErrorGraphics errore = new ErrorGraphics(parent,
                            "One (or both) fields are empty");

                }
            }
        });

        // Per terminare tutto il programma una volta chiusa la la finestra di
        // inizializzazione
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // Chiude tutto
            }
        });
    }

    /**
     * Metodo che rende visibile la pagina che consente all'utente di inserire
     * l'indirizzo ip e la porta del server e che mette in attesa la pagina che l'ha
     * chiamata (cioè quella passata al costruttore).
     * 
     * @return un vettore di stringhe contenente il contenuto dei campi compilati
     *         dall'utente:
     *         <ul>
     *         <li>[0] = ip</li>
     *         <li>[1] = porta</li>
     *         </ul>
     */
    public String[] getIpAndPort() {
        setVisible(true); // blocca finché il dialog è aperto
        return Result; // ritorna valore dopo chiusura
    }

}
