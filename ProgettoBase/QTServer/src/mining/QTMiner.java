package mining;

import java.io.*;

import Exceptions.*;
import data.*;

/**
 * Classe che implementa l'algoritmo di clustering Quality Threshold (che crea i
 * cluster in base al raggio scelto)
 * 
 * @author Lorusso Francesco
 * @author Lupis Matteo
 */
public class QTMiner implements Serializable {
    /**
     * L'insieme di cluster ottenuto utilizzando l'algoritmo Quality Threshold
     */
    private ClusterSet C;

    /**
     * Raggio che descrive la distanza massima dai centroidi alle rispettive tuple
     * (per ogni cluster)
     */
    private double radius;

    /**
     * Costruisce la classe QTMiner
     * 
     * @param radius raggio del clusterset
     */
    public QTMiner(double radius) {
        C = new ClusterSet();
        this.radius = radius;
    }

    /**
     * Costruisce la classe QTMiner leggendo il clusterset dal file il cui
     * percorso + nome è passato come parametro
     * 
     * @param fileName percorso + nome del file da cui leggere il clusterset
     * @throws FileNotFoundException  se non è possibile aprire il file o se
     *                                quest'ultimo non esiste
     * @throws IOException            se c'è stato un errore durante la lettura
     *                                dell'oggetto dal file
     * @throws ClassNotFoundException se non viene trovata la classe dell'oggetto
     *                                serializzato
     */
    public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {

        synchronized (this) {
            FileInputStream file_lettura = new FileInputStream(fileName);

            ObjectInputStream stream = new ObjectInputStream(file_lettura);

            C = (ClusterSet) stream.readObject();

            stream.close();
            file_lettura.close();
        }

    }

    /**
     * Salva sul file (il cui percorso + nome è passato come parametro) l'intero
     * clusterset (cioè l'insieme di tutti i cluster)
     * 
     * @param fileName nome del file in cui salvare il clusterset
     * @throws FileNotFoundException se non è possibile aprire il file o se
     *                               quest'ultimo non esiste
     * @throws IOException           se c'è stato un errore durante la lettura
     *                               dell'oggetto dal file
     */
    public synchronized void salva(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream file = new FileOutputStream(fileName);

        ObjectOutputStream stream = new ObjectOutputStream(file);

        stream.writeObject(C);

        stream.close();

        file.close();
    }

    /**
     * Restituisce l'insieme dei cluster
     * 
     * @return l'insieme dei cluster
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo di Quality Threshold creando i cluster (in base al raggio)
     * e restituendo il numero di cluster identificati
     * 
     * @param data dataset da cui prelevare le tuple da clusterizzare
     * @return numero di cluster identificati
     * @exception ClusteringRadiusException Nel caso in cui il numero di cluster
     *                                      identificati dall'algoritmo QT sia 1
     * @exception EmptyDatasetException     Nel caso in cui il dataset sia vuoto
     */
    public int compute(Data data) throws ClusteringRadiusException, EmptyDatasetException {
        int numclusters = 0;

        if (data.getNumberOfExamples() > 0) {
            boolean isClustered[] = new boolean[data.getNumberOfExamples()];

            for (int i = 0; i < isClustered.length; i++)
                isClustered[i] = false;

            int countClustered = 0;
            while (countClustered != data.getNumberOfExamples()) {

                // Ricerca cluster più popoloso
                Cluster c = buildCandidateCluster(data, isClustered);
                C.add(c);
                numclusters++;

                // Rimuovo tuple clusterizzate da dataset

                for (Integer clusteredTupleId : c) {
                    isClustered[clusteredTupleId] = true;
                }
                countClustered += c.getSize();
            }

            if (numclusters == 1) {
                throw new ClusteringRadiusException();
            }
            return numclusters;
        } else {
            throw new EmptyDatasetException();
        }
    }

    /**
     * Crea un nuovo cluster (il più popoloso tra quelli creabili) attingendo alle
     * tuple non ancora clusterizzate
     * 
     * @param data        dataset da cui prelevare le tuple da clusterizzare
     * @param isClustered array di booleani che indica quali tuple (del dataset)
     *                    devono ancora essere clusterizzate (true = tupla già
     *                    clusterizzata)
     * @return il cluster più popoloso
     */
    private Cluster buildCandidateCluster(Data data, boolean isClustered[]) {
        boolean primoCandidatoTrovato = false;
        int i, j, k;
        for (i = 0; i < isClustered.length && !primoCandidatoTrovato; i++) {
            if (isClustered[i] == false) {
                primoCandidatoTrovato = true;
            }
        }
        i--;

        // si crea il primo cluster
        Cluster candidato = new Cluster(data.getItemSet(i));

        candidato.addData(i);
        for (j = i + 1; j < isClustered.length; j++) {
            if (isClustered[j] == false) {
                if (candidato.getCentroid().getDistance(data.getItemSet(j)) <= radius) {
                    candidato.addData(j);
                }
            }
        }

        // si creano gli altri cluster e si vede il più grande
        for (j = i + 1; j < isClustered.length; j++) {
            if (isClustered[j] == false) {
                Cluster secondoCandidato = new Cluster(data.getItemSet(j));

                secondoCandidato.addData(j);
                for (k = 0; k < j; k++) {
                    if (isClustered[k] == false) {
                        if (secondoCandidato.getCentroid().getDistance(data.getItemSet(k)) <= radius) {
                            secondoCandidato.addData(k);
                        }
                    }
                }
                for (k = j + 1; k < isClustered.length; k++) {
                    if (isClustered[k] == false) {
                        if (secondoCandidato.getCentroid().getDistance(data.getItemSet(k)) <= radius) {
                            secondoCandidato.addData(k);
                        }
                    }
                }
                if (candidato.getSize() < secondoCandidato.getSize())
                    candidato = secondoCandidato;
            }
        }
        return candidato; // cluster più popoloso
    }

    /**
     * Restituisce una stringa contenente il raggio e il clusterset
     * 
     * @param data dataset da cui prelevare le tuple
     * @return stringa contenente il raggio e il clusterset
     */
    public String toString(Data data) {
        String contenuto = new String();
        contenuto = "radius: " + radius + "\n";
        contenuto += C.toString(data);
        return contenuto;
    }

}
