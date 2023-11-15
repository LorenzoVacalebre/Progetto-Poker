///////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE COMUNICAZIONE CONTENENTE TUTTA LA COMUNICAZIONE CON IL PROTOCOLLO TCP TRA CLIENT E SERVER   //
//UTILITA': RICEVIMENTO/INVIO DATI CON I DIVERSI CLIENT, CREAZIONE GIOCO E GIOCATORI, CUORE DEL GIOCO//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;

public class Comunicazione {
    static int NUMERO_GIOCATORI = 1;

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

    //fase
    private int nFase = 0;

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
    public void avviaServer() {
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

        if(NUMERO_GIOCATORI > 1) {
            //controllo/cambio turno giocatore
            if(this.turnoGiocatore == 0)
                this.turnoGiocatore = 1;
            else if(this.turnoGiocatore == 1)
                this.turnoGiocatore = 2;
            else if(this.turnoGiocatore == 2)
                this.turnoGiocatore = 1;
        }
        /* 
        else if(this.turnoGiocatore == 3)
            this.turnoGiocatore = 1;
            */

        //log gioco
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String funzioneRichiesta = in.readLine();
        System.out.println(funzioneRichiesta);

        //creazione nuovo giocatore temporaneo
        Giocatore g = new Giocatore(clientSocket);

        //aggiunta giocatore alla lista di giocatori presenti in partita
        this.listaGiocatori.aggiungiGiocatore(g);

        //metodo per chiudere la connessione
        // clientSocket.close();
    
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
        Socket s = new Socket();

        if(NUMERO_GIOCATORI > 1) {
        if(this.turnoGiocatore == 1)
            this.turnoGiocatore = 2;
        else if(this.turnoGiocatore == 2)
            this.turnoGiocatore = 1;

        //inzializzazione socket con client utile alla gestione del flusso dei dati
        if(this.turnoGiocatore == 1)
            s = this.listaGiocatori.getGiocatore(0).getSocket();
        if(this.turnoGiocatore == 2)
            s = this.listaGiocatori.getGiocatore(1).getSocket();

        }else
            s = this.listaGiocatori.getGiocatore(0).getSocket();

        //controllo se il giocatore desidera abbandonare la partita
        //this.controlloAbbandonaPartita(s);

        //salvataggio client che effettua richiesta
        int posClientCheEffettuaRichiesta = this.listaGiocatori.trovaPosizioneClient(s);

        //se il giocatore è ancora presente nel round
        if(this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).getStatusPresenza() == true)
        {
            this.gioco.setPosGiocatoreEffRic(posClientCheEffettuaRichiesta);
            this.gioco.setSocketClientTmp(s);

            //è il turno del giocatore 
            this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(true);
            
            /////APPOSTO///// DISTRIBUZIONE DUE CARTE A TUTTI I GIOCATORI // PRIMA FASE
            if(this.gioco.statoRound() == false) {

                //inserimento carte dal mazzo alla mano del giocatore
                
                this.gioco.trovaGiocatoreEInserisciCartaInMano();
                /////
                this.gioco.trovaGiocatoreEInserisciCartaInMano();

                //se il client che ha effettuato la connessione è il terzo controllo se il round è già attivo
                if(this.listaGiocatori.trovaPosizioneClient(s) == NUMERO_GIOCATORI - 1) {
                    //round a true
                    this.gioco.setStatusRoundTrue();
                    //non è il turno del giocatore 
                    this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(false);
                    this.nFase = 2;
                }
                else
                    //non è il turno del giocatore 
                    this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(false);
            }
            else
            {
                //salvataggio richiesta di uno dei client nel gioco
                //Allegamento puntata (punta/*numero*)
                this.riceviRichiestaDalClient(s);
                switch(this.nFase)
                {
                    case 2:
                        //esegui la mano
                        this.eseguiTurno(s,posClientCheEffettuaRichiesta);
                        break;
                    case 3: 
                        //aggiungo carta al flop
                        this.gioco.distribuisciFlop();

                        //invio a tutti il nuovo flop
                        this.inviaInfoATutti();

                        this.nFase++;
                        break;
                    case 4:
                        //esegui la mano
                        this.eseguiTurno(s,posClientCheEffettuaRichiesta);
                        break;
                    case 5:
                        //aggiungo carta al flop
                        this.gioco.aggiungiCartaFlop();

                        //invio a tutti il nuovo flop
                        this.inviaInfoATutti();
                        this.nFase++;
                        break;
                    case 6:
                        //esegui la mano
                        this.eseguiTurno(s,posClientCheEffettuaRichiesta);
                        break;
                    case 7:
                        //aggiungo carta al flop
                        this.gioco.aggiungiCartaFlop();

                        //invio a tutti il nuovo flop
                        this.inviaInfoATutti();
                        this.nFase++;
                        break;
                    case 8:
                        //esegui la mano
                        this.eseguiTurno(s,posClientCheEffettuaRichiesta);
                        break;
                    case 9:
                        //visualizzazione tutte le carte
                        this.gioco.showDown();

                        //assegno il piatto al vincitore
                        this.gioco.assegnazionePiatto();
                        break;
                    default:
                        break;
                }
            }
        } 

        //metodo per chiudere la connessione
        //this.chiudiConnessione(clientSocket);

        //copio la lista modificata dal gioco nella comunicazione
        this.listaGiocatori = this.gioco.getListaGiocatori();
    }

    //metodo utile alla chiusura di una connessione 
    private void chiudiConnessione(Socket socket) throws IOException {
        socket.close();
    }

    //metodo per controllare se un client ha richiesto di abbandonare la partita 
    public boolean controlloAbbandonaPartita(Socket client) throws IOException
    {
        //se il giocatore ha richiesto di abbandonare la partita
        if(this.riceviRichiestaDalClientString(client).equals("abbandonaPartita")){
            //rimuovo giocatore dalla partita
            this.listaGiocatori.pullGiocatore(client);
            //chiudi comunicazione
            client.close();
            //giocatore eliminato
            return true;
        }
        return false;
    }

    //salvataggio funzione richiesta da uno dei client nel gioco
    public void riceviRichiestaDalClient(Socket clientSocket) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String funzioneRichiesta = in.readLine();
            this.gioco.setFunzioneRichiesta(funzioneRichiesta);
        }
    } 

    //restituisce la richiesta del client come una stringa
    public String riceviRichiestaDalClientString(Socket clientSocket) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String funzioneRichiesta = in.readLine();
            return funzioneRichiesta;
        }
    } 

    public void invioInformazioniAlClient(Socket clientSocket, String messaggio) throws IOException
    {
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        output.println(messaggio); //cosi facendo invio dati al server
    }

    //metodo per inviare le informazioni a ogni client
    private void inviaInfoATutti() throws IOException
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
            invioInformazioniAlClient(this.listaGiocatori.getGiocatore(i).getSocket(), "flop/" + this.gioco.flopToString());
    }

    //metodo per inviare le informazioni a ogni client
    public void inviaInfoATuttiFine() throws IOException
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
            invioInformazioniAlClient(this.listaGiocatori.getGiocatore(i).getSocket(), "fine");
    }

    //metodo per scoprire tutte le carte
    public void inviaInfoATuttiScopriCarte() throws IOException
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
            invioInformazioniAlClient(this.listaGiocatori.getGiocatore(i).getSocket(), "scopriTutto");
    }

    //metodo per eseguire un turno
    private void eseguiTurno(Socket clientSocket, int posClientCheEffettuaRichiesta) throws IOException
    {
        if((this.listaGiocatori.trovaPosizioneClient(clientSocket) != NUMERO_GIOCATORI - 1))
        {
            //se il gioco è attivo
            if(this.gioco.getStatus() == true)
                this.gioco.eseguiMano(); //esegui la mano
            //non è il turno del giocatore 
            this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(false);
            this.nFase++;
        }
        else 
        {
            //se il gioco è attivo
            if(this.gioco.getStatus() == true)
                this.gioco.eseguiMano(); //esegui la mano

            //non è il turno del giocatore 
            this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(false);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////
    //METODO UTILE AI TEST PER VERIFICARE IL FUNZIONAMENTO DELLA COMUNICAZIONE TCP//
    ////////////////////////////////////////////////////////////////////////////////
    /*private void comunicazioneTest()
    {
        try {
            System.out.println("Server in ascolto sulla porta " + this.port);

            //attendi la connessione da un client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connessione accettata da " + clientSocket.getInetAddress());

            //ottieni lo stream di input dal socket
            InputStream inputStream = clientSocket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //leggi il messaggio inviato dal client
            String clientMessage = bufferedReader.readLine();
            System.out.println("Messaggio ricevuto dal client: " + clientMessage);

            //chiudi la connessione
            this.chiudiConnessione(clientSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    ////////////////////////////////////////////////////////////////////////////////
}
