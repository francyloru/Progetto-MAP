package Graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe che modella la finestra di dialogo con l'utente nella quale viene
 * mostrato il messaggio mandato dal server, contenente i Cluster ottenuti
 * dall'algortimo QT (o i cluster che erano contenuti in un file)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class ShowResult extends JDialog {

    /**
     * Bottone che consente all'utente di confermare di aver letto il messaggio
     * mandato dal server e di tornare indietro per poter scegliere una nuova
     * opzione tra quelle presenti nel menu principale
     */
    private JButton bottone_esci;

    /**
     * Costruttore della classe ShowResult che crea la finestra (allineata
     * orizzontalmente con la pagina chiamante, cioè quella del menù principale)
     * nella quale è contenuta la risposta del server, cioè una stringa
     * rappresentante i cluster letti dal file o ottenuti in seguito all'esecuzione
     * dell'algoritmo QT.
     * La pagina resta attiva (e impedisce l'accesso alla pagina chiamante) fino a
     * quando non viene premuto il bottone di conferma di avvenuta lettura
     * 
     * @param parent                frame che ha fatto richiesta di mostrare il
     *                              messaggio del server
     * @param risultato_da_mostrare messaggio, spedito dal server, che si vuol
     *                              mostrare all'utente
     */
    public ShowResult(Frame parent, String risultato_da_mostrare) {
        super(parent, "Result", true);
        // setSize(500, 500);
        // setResizable(false);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JTextArea area_risultato = new JTextArea(risultato_da_mostrare);
        area_risultato.setEditable(false);
        area_risultato.setCaretColor(area_risultato.getBackground());

        JScrollPane contenitore_area_risultato = new JScrollPane(area_risultato,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contenitore_area_risultato.setBorder(new EmptyBorder(10, 10, 10, 10));

        form.add(contenitore_area_risultato);

        JPanel riga_bottone = new JPanel();
        bottone_esci = new JButton("Ok");
        riga_bottone.add(bottone_esci);
        form.add(riga_bottone);
        add(form);

        bottone_esci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // sposta più in basso
        // setLocation(getX(), getY() + 20);

        setMinimumSize(new Dimension(400, 10));
        pack(); // adatta finestra al contenuto
        setResizable(false);
        setLocationRelativeTo(parent);

        // Per impostare il limite in altezza
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int maxHeight = 600; // limite massimo in altezza
                int h = Math.min(getHeight(), maxHeight);
                setSize(getWidth(), h);
            }
        });

        setVisible(true);

    }
}
