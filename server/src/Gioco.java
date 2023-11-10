//////////////////////////////////////////////////////////////////////////////////////
//CLASSE GIOCO CONTENENTELE INFORMAZIONI PIU' IMPORTANTI PER GESTIRE IL GIOCO IN SE'//
//UTILITA': GESTIRA' BENE O MALE TUTTO IL GIOCO DEL POKER                           //
//////////////////////////////////////////////////////////////////////////////////////

public class Gioco {
    //lista giocatori
    private GestioneGiocatori listaGiocatori;

    //informazioni temporanee
    private int giocatoreEffRic;
    private String funzioneRichiesta;

    //mazzi utili al gioco
    private Mazzo mazzoDaGioco, mazzoCarteScartate;

    //stato del gioco TRUE -> gioco in esecuzione // FALSE -> gioco fermo
    private boolean status = false;

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////

    //costruttore utile solamente per ottenere la lista dei giocatori
    public Gioco(GestioneGiocatori listaCompleta) { this.listaGiocatori = listaCompleta; }

    //metodo utile ad impostare il giocatore temporaneo che ha effettuato una determinata richiesta
    public void setGiocatoreEffRic(int nG){ this.giocatoreEffRic = nG; }

    //metodo utile a salvare la funzione richiesta dal giocatore temporaneo
    public void setFunzioneRichiesta(String fR){ this.funzioneRichiesta = fR; }

    //metodo utile ad impostare lo stato del gioco a true
    public void setStatusTrue(){ this.status = true; }

    //metodo utile ad impostare lo stato del gioco a false
    public void setStatusFalse(){ this.status = false; }

    //metodo utile a restituire il client
    public boolean getStatus() { return this.status;}

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

    //metodo utile a giocare il turno al client che ha effettuato la richiesta al server
    public void eseguiMano()
    {
        //TODO -> IMPARARE IL FUNZIONAMENTO DEL POKER PER IMPLEMENTAZIONE
        //Trovare il modo di gestire i giocatori all'interno della lista, in seguito gestire 
        //la mano del giocatore stesso dato il client che ha effettuato una richiesta
    }

}
