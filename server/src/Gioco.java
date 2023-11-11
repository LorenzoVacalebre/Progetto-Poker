//////////////////////////////////////////////////////////////////////////////////////
//CLASSE GIOCO CONTENENTELE INFORMAZIONI PIU' IMPORTANTI PER GESTIRE IL GIOCO IN SE'//
//UTILITA': GESTIRA' BENE O MALE TUTTO IL GIOCO DEL POKER                           //
//////////////////////////////////////////////////////////////////////////////////////

import java.io.IOException;
import java.net.Socket;

public class Gioco {
    //lista giocatori
    private GestioneGiocatori listaGiocatori;

    //informazioni temporanee
    private int posGiocatoreEffRic;
    private String funzioneRichiesta;
    private Socket socketClientTmp;
    private Comunicazione comunicazioneTmp;

    //mazzi utili al gioco
    private Mazzo mazzoDaGioco, mazzoCarteScartate;

    //stato del gioco TRUE -> gioco in esecuzione // FALSE -> gioco fermo
    private boolean statusGioco = false;
    
    //stato del round TRUE -> i giocatori non devono pescare le due carte
    //FALSE -> i giocatori devono pescare le due carte
    private boolean statusRound = false;

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////

    //costruttore utile solamente per ottenere la lista dei giocatori
    public Gioco(GestioneGiocatori listaCompleta) { this.listaGiocatori = listaCompleta; }

    //metodo utile ad impostare il giocatore temporaneo che ha effettuato una determinata richiesta
    public void setPosGiocatoreEffRic(int nG){ this.posGiocatoreEffRic = nG; }

    //metodo utile a salvare la funzione richiesta dal giocatore temporaneo
    public void setFunzioneRichiesta(String fR){ this.funzioneRichiesta = fR; }

    //metodo utile ad impostare lo stato del gioco a true
    public void setStatusTrue(){ this.statusGioco = true; }

    //metodo utile ad impostare lo stato del gioco a false
    public void setStatusFalse(){ this.statusGioco = false; }

    //metodo utile ad impostare il client del server che ha effettuato una richiesta
    public void setSocketClientTmp(Socket tmp) { this.socketClientTmp = tmp; }

    //metodo utile a restituire il client
    public boolean getStatus() { return this.statusGioco;}

    //metodo utile a ottenere indietro la lista dei giocatori modificata
    public GestioneGiocatori getListaGiocatori(){ return this.listaGiocatori; }

    //ritorna lo stato del round
    public boolean isStatusRound() {return statusRound; }

    //setta lo stato del round a false
    public void setStatusRoundFalse() {this.statusRound = false; }

    //setta lo stato del round a true
    public void setStatusRoundTrue() {this.statusRound = true; }

    //metodo utile ad avviare il gioco
    public void creaMazzi()
    {
        //mazzo utilizzato per giocare
        this.mazzoDaGioco = new Mazzo();
        //riempimento mazzo da gioco
        this.mazzoDaGioco.riempiMazzo();
        //mischiamento mazzo da gioco
        this.mazzoDaGioco.mischiaMazzo();

        //mazzo utilizzato per inserirci le carte scartate
        this.mazzoCarteScartate = new Mazzo();
    }

    //metodo per distribuire le carte a tutti i giocatori
    private Carta distribuisciCarta()
    {
        return this.mazzoDaGioco.pull();
    }

    //metodo utile a ricavare il giocatore a cui bisogna distribuire la carta
    public void trovaGiocatoreEInserisciCartaInMano() throws IOException
    {
        this.listaGiocatori.ottieniGiocatore(this.socketClientTmp).getManoGiocatore().push(this.distribuisciCarta());
        int posCartaInserita = this.listaGiocatori.ottieniGiocatore(this.socketClientTmp).getManoGiocatore().size() - 1;
        Carta tmp = this.listaGiocatori.ottieniGiocatore(this.socketClientTmp).getManoGiocatore().get(posCartaInserita);

        //invio al client temporaneo l'informazione utile, in questo caso viene svuotata la sua mano
        this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, tmp.getNumero() + ";" + tmp.getSeme() + ";" + tmp.getIsFacedUp());
    }

    //metodo utile a restituire lo stato del round
    public boolean statoRound()
    {
        return this.statusRound;
    }

    //metodo utile a svuotare tutte le mani dopo che il round viene terminato
    public void svuotaMani()
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
            this.listaGiocatori.getGiocatore(i).getManoGiocatore().svuotaMano(this.mazzoCarteScartate);
    }

    //metodo utile a giocare il turno al client che ha effettuato la richiesta al server
    public void eseguiMano() throws IOException
    {
        //Trovare il modo di gestire i giocatori all'interno della lista, in seguito gestire 
        //la mano del giocatore stesso dato il client che ha effettuato una richiesta
        switch(this.funzioneRichiesta)
        {
            case "scommetti":


                break;
            case "passa":
                //inserimento carte scartate nel mazzo corretto
                this.mazzoCarteScartate.pushMano(this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getManoGiocatore());

                //eliminazione mano del giocatore che ha richiesto la funzione passa
                this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getManoGiocatore().svuotaMano(this.mazzoCarteScartate);

                //invio al client temporaneo l'informazione utile, in questo caso viene svuotata la sua mano
                this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, "svuotaMano");

                //il giocatore non parteciperà più al round
                this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).setStatusPresenza(false);

                break;
            case "rilancia":


                break;

            case "showdown":
                    //TODO -> visulizzazione generale carte

                    this.statusRound = false;
                    this.svuotaMani();

                    //invio al client temporaneo l'informazione utile, in questo caso viene svuotata la sua mano
                    this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, "roundTerminato");
                break;

            default:
                break;
        }
    }
}
