# Progetto-MAP
Il progetto presentato è stato sviluppato per il corso di Metodi Avanzati di Programmazione 2024-2025 e ha 
come obiettivo la realizzazione di un sistema client–server per l’esecuzione dell’<a href="https://www.ibm.com/it-it/think/topics/unsupervised-learning#2014952965:~:text=in%20modo%20efficace.-,Clustering,-Il%20clustering%20%C3%A8">algoritmo QT 
(Quality Threshold Clustering)</a> su dati memorizzati in un database.

L’architettura è suddivisa in due componenti principali: un client, che invia le richieste e un server,
che gestisce l’accesso al database ed esegue l’algoritmo di clustering.

<h1>Struttura del progetto</h1>

Il progetto è stato realizzato in una forma base ed una con estensione:

<ol>
  
  <li>
    <h2>Progetto Base</h2>
    Il progetto base implementa le funzionalità fondamentali del sistema.
  
  Il <b>Client</b>, dopo aver indicato i dati necessari per collegarsi al Server, invia al Server una richiesta contenente: 
  <ul>
    <li>il nome della tabella da analizzare</li>
    <li>il valore del raggio da utilizzare per l’algoritmo QT</li>
  </ul>

  Il <b>Server</b> riceve la richiesta del Client e:  
  <ul>
    <li>Accede al database fornito dalla docente</li>
    <li>Il server esegue l’algoritmo Quality Threshold Clustering sulla tabella indicata</li>
    <li>I risultati dei cluster generati vengono inviati al client, il quale li mostrerà all'utente</li>
    <li>Salva il Clusterset prodotto dall'algoritmo file dedicato, per poter essere visionato in seguito</li>
  </ul>
    
  </li>
  <li>
    <h2>Progetto Esteso</h2>
    Il progetto con estensione implementa un’interfaccia grafica lato Client, che consente all’utente di inserire i parametri per accedere al server e per eseguire (oltre a visualizzare i risultati) l'algoritmo QT (oltre a recuperare i Cluster già salvati sul Server da precedenti interazioni) in modo più intuitivo
  </li>
</ol>

<h1>Obiettivo</h1>
L’obiettivo del progetto è applicare e consolidare l’utilizzo dei principali costrutti della programmazione orientata agli oggetti, con particolare riferimento al linguaggio Java, supportato dall’uso di SQL per l’accesso e la gestione dei dati. Il progetto mira inoltre all’implementazione di un’architettura client–server basata su socket, alla gestione della comunicazione tra i due componenti e alla separazione delle responsabilità tra interfaccia, logica applicativa ed elaborazione dei dati.

