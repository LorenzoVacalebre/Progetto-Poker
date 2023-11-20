///////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE COMUNICAZIONE CONTENENTE TUTTA LA COMUNICAZIONE CON IL PROTOCOLLO TCP TRA CLIENT E SERVER   //
//UTILITA': RICEVIMENTO/INVIO DATI CON I DIVERSI CLIENT, CREAZIONE GIOCO E GIOCATORI, CUORE DEL GIOCO//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;

import java.net.*;

public class Comunicazione {
    static int NUMERO_GIOCATORI = 2;

    //socket del server
    private ServerSocket serverSocket;

    //porta di collegamento
    private int port;

    //numero client connessi
    private int numeroDiClientConnessi;

    //lista dei giocatori presenti in partita utile a salvare le varie socket
    private GestioneGiocatori listaGiocatori;

    //gioco
    private Gioco gioco;

    //giocatore che deve giocare
    private int turnoGiocatore = 0;

    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    //costruttore di default (utile a main)
    public Comunicazione(int port) {
        this.port = port;
        this.numeroDiClientConnessi = 0;
        this.listaGiocatori = new GestioneGiocatori();
    }

    //metodo per avviare la prima volta il server
    public void avviaServer() throws InterruptedException {
        try {
            //inzializzazione socket del server
            this.serverSocket = new ServerSocket(port);

            /* RICHIAMO METODO PER TESTARE IL FUNZIONAMENTO DELLA COMUNICAZIONE TCP
            while(true)
                this.comunicazioneTest();
            */

            //mantenimento server in ascolto
            while (true) {
                //accetta connessione da client diversi fino a quando questi diventano 3
                if (this.numeroDiClientConnessi < NUMERO_GIOCATORI) {
                    this.gestisciConnessioneSingoloClient();//metodo gestione singole connessioni iniziali
                } else {
                    if(this.gioco.getStatus() == false) {
                        this.gioco.setStatusTrue(); //set stato del gioco a true
                        this.gioco.creaMazzi(); //creazione mazzi
                    }
                    else 
                        this.leggiRichiesteDeiClient(); //metodo utile a leggere continuamente tutte le richieste del client
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo gestione singole connessioni iniziali
    private void gestisciConnessioneSingoloClient() throws IOException {

        //inizializzazione socket con il client utile alla gestione del flusso dei dati
        Socket clientSocket = this.serverSocket.accept();

        if (NUMERO_GIOCATORI > 1) {
            //controllo per cambiare il turno del giocatore
            this.turnoGiocatore = (this.turnoGiocatore + 1) % NUMERO_GIOCATORI;
        }

        //visualizzazione quali giocatori entrano in partita
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String funzioneRichiesta = in.readLine();
        System.out.println(funzioneRichiesta);

        //creazione nuovo giocatore temporaneo
        Giocatore g = new Giocatore(clientSocket);

        //aggiunta giocatore alla lista di giocatori presenti in partita
        this.listaGiocatori.aggiungiGiocatore(g);
    
        //incremento numero di client connessi al server
        this.numeroDiClientConnessi++;
        
        //inserimento lista completa dei 3 giocatori all'interno del gioco
        if(numeroDiClientConnessi == NUMERO_GIOCATORI){
            this.gioco = new Gioco(listaGiocatori);//inizio nuovo gioco
        }
    }

    //metodo utile a leggere continuamente tutte le richieste del client
    private void leggiRichiesteDeiClient() throws IOException {

        //definizio socket utile alla trasmissione di dati con i vari giocatori della partita
        Socket s = null;
        int posClientCheEffettuaRichiesta;

        //se il numero di giocatori è superiore a 1 
        if (NUMERO_GIOCATORI > 1) {
            //imposta i turni nel modo corretto
            this.turnoGiocatore = (this.turnoGiocatore % NUMERO_GIOCATORI) + 1;
            //imposta socket corretta
            s = this.listaGiocatori.getGiocatore(this.turnoGiocatore - 1).getSocket();
        } else {
            s = this.listaGiocatori.getGiocatore(0).getSocket();
        }
    
        //salvataggio client che effettua richiesta
        posClientCheEffettuaRichiesta = this.listaGiocatori.trovaPosizioneClient(s);

        this.gioco.setPosGiocatoreEffRic(posClientCheEffettuaRichiesta);
        this.gioco.setSocketClientTmp(s);

        //è il turno del giocatore 
        this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(true);
        
        this.gioco.trovaGiocatoreEInserisciCartaInMano();
        /////
        this.gioco.trovaGiocatoreEInserisciCartaInMano();
        /////
        this.gioco.distribuisciFlop();
        /////
        this.inviaflop(posClientCheEffettuaRichiesta);
        
        if(posClientCheEffettuaRichiesta == this.listaGiocatori.size() - 1)
        {
            this.invioInformazioniAlClient(s, "true");
            /////
            this.riceviRichiestaDalClient(s);
            /////
            this.eseguiTurno(s, posClientCheEffettuaRichiesta);
            /////
            this.invioInformazioniAlClient(s, "false");

            //assegno il piatto al vincitore
            //s = this.gioco.assegnazionePiatto();

            //elimino tutte le carte
            //this.gioco.showDown();

            //this.inviaInfoATutti(s);
        }
        else 
        {
            this.invioInformazioniAlClient(s, "true");
            /////
            this.riceviRichiestaDalClient(s);
            /////
            this.eseguiTurno(s, posClientCheEffettuaRichiesta);
            /////
            this.invioInformazioniAlClient(s, "false");

        }

        //copio la lista modificata dal gioco nella comunicazione
        this.listaGiocatori = this.gioco.getListaGiocatori();
    }

    //salvataggio funzione richiesta da uno dei client nel gioco
    public void riceviRichiestaDalClient(Socket clientSocket) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String funzioneRichiesta = in.readLine();
        System.out.println(funzioneRichiesta);
        this.gioco.setFunzioneRichiesta(funzioneRichiesta);
    } 

    public void invioInformazioniAlClient(Socket clientSocket, String messaggio) throws IOException
    {
        try {
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println(messaggio);
        } catch (IOException e) {
            System.out.println("ERRORE INVIO INFORMAZIONI!");
            // Gestione delle eccezioni, ad esempio, log o altre azioni necessarie
            e.printStackTrace();
        }
    }
    //metodo per inviare la flop singolarmente
    public void inviaflop(int posTmp) throws IOException
    {
        invioInformazioniAlClient(this.listaGiocatori.getGiocatore(posTmp).getSocket(), this.gioco.flopToString());
    }

    //metodo per inviare le informazioni a ogni client
    public void inviaInfoATutti(Socket vincitore) throws IOException
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            if(this.listaGiocatori.getGiocatore(i).getSocket().getInetAddress().equals(vincitore.getInetAddress()))
                this.invioInformazioniAlClient(vincitore, "vincita/" + this.gioco.getScommessaTot() + "/true");
            else
                this.invioInformazioniAlClient(vincitore, "vincita/" + this.gioco.getScommessaTot() + "/false");
        }
    }

    //metodo per eseguire un turno
    private void eseguiTurno(Socket clientSocket, int posClientCheEffettuaRichiesta) throws IOException
    {
        //se il gioco è attivo
        this.gioco.eseguiMano(); //esegui la mano
        
        //non è il turno del giocatore 
        this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(false);
    }
}
