import java.io.*;
import java.net.*;

public class Comunicazione {
    //attributi utili
    private ServerSocket serverSocket;
    private int port;
    private int numeroDiClientConnessi;
    private GestioneGiocatori listaGiocatori;
    private Gioco gioco;
    
    //costruttore di default
    public Comunicazione(int port) {
        this.port = port;
        this.numeroDiClientConnessi = 0;
        this.listaGiocatori = new GestioneGiocatori();
    }

    public void avviaServer() {
        try {
            //inzializzazione socket del server
            serverSocket = new ServerSocket(port);

            //mantenimento server in ascolto
            while (true) {
                //accetta connessione da client diversi fino a quando questi diventano 3
                if (this.numeroDiClientConnessi < 3) {
                    gestisciConnessioneSingoloClient();//metodo gestione singole connessioni iniziali
                } else {
                    mantieniServerInAscolto(); //metodo utile a leggere continuamente tutte le richieste del client
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo gestione singole connessioni iniziali
    private void gestisciConnessioneSingoloClient() throws IOException {
        //inizializzazione socket con il client utile alla gestione del flusso dei dati
        Socket clientSocket = serverSocket.accept();
    
        //creazione nuovo giocatore temporaneo
        Giocatore g = new Giocatore(clientSocket);

        //aggiunta giocatore alla lista di giocatori presenti in partita
        this.listaGiocatori.push(g);

        // Metodo per chiudere la connessione
        chiudiConnessione(clientSocket);
    
        //incremento numero di client connessi al server
        this.numeroDiClientConnessi++;

        //inserimento lista completa dei 3 giocatori all'interno del gioco
        if(numeroDiClientConnessi == 3)
            this.gioco = new Gioco(listaGiocatori);//inzio nuovo gioco
    }

    //metodo utile a leggere continuamente tutte le richieste del client
    private void mantieniServerInAscolto() throws IOException {

        //inzializzazione socket con client utile alla gestione del flusso dei dati
        Socket clientSocket = serverSocket.accept();

        //salvataggio client che effettua richiesta
        int clientCheEffettuaRichiesta = this.listaGiocatori.trovaClient(clientSocket);
        this.gioco.setGiocatoreEffRic(clientCheEffettuaRichiesta);

        //salvataggio richiesta di uno dei client nel gioco
        invioRichiestaAlGioco(clientSocket);

        //metodo per chiudere la connessione
        chiudiConnessione(clientSocket);
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
    private void invioRichiestaAlGioco(Socket clientSocket) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String funzioneRichiesta = in.readLine();
            this.gioco.setFunzioneRichiesta(funzioneRichiesta);
        }
    }    
}