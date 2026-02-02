package Graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe che modella la finestra di dialogo con l'utente nella quale deve
 * essere mostrato uno specifico messaggio di errore.
 * La pagina presenta un bottone che, se premuto, riporta la navigazione sulla
 * pagina precedente (cioè su quella nella quale è stato generato l'errore)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class ErrorGraphics extends JDialog {

    /**
     * Bottone che consente all'utente di confermare di aver letto il messaggio di
     * errore e di tornare indietro
     */
    private JButton bottone_errore;

    /**
     * Costruttore della classe ErrorGraphics che crea la finestra (allineata
     * orizzontalmente con la pagina che ha generato l'errore) che contiene il
     * messaggio di errore da mostrare all'utente.
     * La pagina resta attiva (e impedisce l'accesso alla pagina chiamante) fino a
     * quando non viene premuto il bottone di conferma di avvenuta lettura
     * 
     * @param parent    frame nel quale è stato generato l'errore
     * @param messaggio messaggio di errore da mostrare all'utente
     */
    public ErrorGraphics(Frame parent, String messaggio) {
        super(parent, "Problems", true);
        setResizable(false);
        // setSize(500, 500);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JPanel riga_errore = new JPanel();
        riga_errore.setLayout(new FlowLayout());
        JLabel errore_rosso = new JLabel("Error:");
        errore_rosso.setForeground(Color.RED);
        riga_errore.add(errore_rosso);
        JLabel area_messaggio = new JLabel(messaggio);
        // area_messaggio.setBackground(Color.white);
        // area_messaggio.setEditable(false);
        // area_messaggio.setCaretColor(area_messaggio.getBackground());

        riga_errore.add(area_messaggio);
        riga_errore.setBorder(new EmptyBorder(20, 15, 20, 15));
        form.add(riga_errore);
        JPanel riga_bottone = new JPanel();
        bottone_errore = new JButton("Ok");
        riga_bottone.add(bottone_errore);
        form.add(riga_bottone);
        add(form);
        pack();

        bottone_errore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        // sposta più in basso
        setLocation(getX(), getY() + 20);
        setVisible(true);
    }
}
