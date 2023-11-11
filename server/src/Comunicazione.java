///////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE COMUNICAZIONE CONTENENTE TUTTA LA COMUNICAZIONE CON IL PROTOCOLLO TCP TRA CLIENT E SERVER   //
//UTILITA': RICEVIMENTO/INVIO DATI CON I DIVERSI CLIENT, CREAZIONE GIOCO E GIOCATORI, CUORE DEL GIOCO//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;

public class Comunicazione {
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
                if (this.numeroDiClientConnessi < 3) {
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

        //controllo se un client prova a collegarsi su una socket già presente 
        //(non dovrebbe poter succedere, controllo per maggiore sicurezza)
        if(this.listaGiocatori.controllaDuplicati(clientSocket) == true)
            this.chiudiConnessione(clientSocket);
        else {
            //creazione nuovo giocatore temporaneo
            Giocatore g = new Giocatore(clientSocket);

            //aggiunta giocatore alla lista di giocatori presenti in partita
            this.listaGiocatori.aggiungiGiocatore(g);

            //metodo per chiudere la connessione
            this.chiudiConnessione(clientSocket);
        
            //incremento numero di client connessi al server
            this.numeroDiClientConnessi++;
        }

        //inserimento lista completa dei 3 giocatori all'interno del gioco
        if(numeroDiClientConnessi == 3){
            this.gioco = new Gioco(listaGiocatori);//inzio nuovo gioco
        }
    }

    //metodo utile a leggere continuamente tutte le richieste del client
    private void leggiRichiesteDeiClient() throws IOException {

        //inzializzazione socket con client utile alla gestione del flusso dei dati
        Socket clientSocket = this.serverSocket.accept();

        //salvataggio client che effettua richiesta
        int posClientCheEffettuaRichiesta = this.listaGiocatori.trovaPosizioneClient(clientSocket);
        this.gioco.setPosGiocatoreEffRic(posClientCheEffettuaRichiesta);
        this.gioco.setSocketClientTmp(clientSocket);

        //è il turno del giocatore 
        this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).setUrTurn(true);

        //salvataggio richiesta di uno dei client nel gioco
        this.riceviRichiestaDalClient(clientSocket);

        //se è il primo giocatore
        //se il giocatore è ancora presente nel round
        if(this.listaGiocatori.getGiocatore(posClientCheEffettuaRichiesta).getStatusPresenza() == true)
        {
            if(this.gioco.statoRound() == false) {
                //inserimento carte dal mazzo alla mano del giocatore
                this.gioco.trovaGiocatoreEInserisciCartaInMano();
                this.gioco.trovaGiocatoreEInserisciCartaInMano();

                //se il client che ha effettuato la connessione è il terzo avvio il round
                if(this.listaGiocatori.trovaPosizioneClient(clientSocket) == 2)
                {
                    this.gioco.setStatusRoundTrue();
                    //metodo per chiudere la connessione
                    this.chiudiConnessione(clientSocket);
                }
                else
                {
                    //metodo per chiudere la connessione
                    this.chiudiConnessione(clientSocket);
                }
            }else
            {
                //se il gioco è attivo
                if(this.gioco.getStatus() == true)
                    this.gioco.eseguiMano();

                //metodo per chiudere la connessione
                this.chiudiConnessione(clientSocket);
            }
        } 
        else
        {
            //metodo per chiudere la connessione
            this.chiudiConnessione(clientSocket);
        }

        //copio la lista modificata dal gioco nella comunicazione
        this.listaGiocatori = this.gioco.getListaGiocatori();
    }

    //metodo utile alla chiusura di una connessione 
    private void chiudiConnessione(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //salvataggio funzione richiesta da uno dei client nel gioco
    public void riceviRichiestaDalClient(Socket clientSocket) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String funzioneRichiesta = in.readLine();
            this.gioco.setFunzioneRichiesta(funzioneRichiesta);
            in.close();
        }
    } 

    public void invioInformazioniAlClient(Socket clientSocket, String messaggio) throws IOException
    {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(messaggio.getBytes());
    } 

    ////////////////////////////////////////////////////////////////////////////////
    //METODO UTILE AI TEST PER VERIFICARE IL FUNZIONAMENTO DELLA COMUNICAZIONE TCP//
    ////////////////////////////////////////////////////////////////////////////////
    private void comunicazioneTest()
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
    ////////////////////////////////////////////////////////////////////////////////
}
