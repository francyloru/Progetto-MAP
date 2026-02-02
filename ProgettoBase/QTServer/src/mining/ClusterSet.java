package mining;

import java.io.Serializable;
import java.util.*;
import data.*;

/**
 * Classe che modella un insieme di cluster
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class ClusterSet implements Iterable<Cluster>, Serializable {
    /**
     * Insieme contenente i cluster
     */
    private Set<Cluster> C;

    /**
     * Costruisce la classe ClusterSet
     */
    ClusterSet() {
        C = new TreeSet<>();
    }

    /**
     * Aggiunge il cluster passato come parametro all'insieme dei cluster
     * 
     * @param c il cluster da aggiungere all'insieme
     */
    void add(Cluster c) {
        C.add(c);
    }

    /**
     * Restituisce un iterator che permette di muoversi tra i cluster del clusterset
     * 
     * @return iterator che permette di muoversi tra i cluster del clusterset
     */
    public Iterator<Cluster> iterator() {
        return C.iterator();
    }

    /**
     * Restituisce la stringa contenente lo stato dei centroidi di ciascun cluster
     * 
     * @return stringa contenente lo stato dei centroidi
     */
    @Override
    public String toString() {
        String centroidi = new String();
        int i = 1;
        for (Cluster cluster : C) {
            centroidi += i + ":Centroid=(" + cluster.getCentroid() + ")\n";
            i++;
        }
        return centroidi;
    }

    /**
     * Restituisce lo stato dell'intero clusterset (inteso come lo stato dei singoli
     * cluster)
     * 
     * @param data il dataset da cui prelevare le tuple appartenenti ai cluster del
     *             clusterset
     * @return stringa contenente lo stato delle tuple del cluster
     */
    public String toString(Data data) {
        String str = "";
        int i = 0;
        for (Cluster cluster : C) {
            // ----------------------------------------------------------------------------------------------------------------
            // mettere instance of
            // temp = (Cluster) it.next();
            if (cluster != null) {
                str += (i + 1) + ":" + cluster.toString(data) + "\n";
            }
            i++;
        }
        return str;
    }
}
