/////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE GIOCATORE CONTENENTE LE INFORMAZIONI UTILI COME LA SOCKET DEI CLIENT E LA PROPRIA MANO//
//UTILITA': PONTE TRA GIOCO E MANO DEL GIOCATORE PER LA GESTIONE DEL GIOCO IN SE               //
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.Socket;

public class Giocatore {
    //salvataggio socket determinato giocatore
    private Socket socketDelGiocatore;

    //oggetto ManoGiocatore per tenere memorizzata la mano di quest'ultimo continuando ad aggiornarla
    private ManoGiocatore manoGiocatore;

    //costruttore di default
    public Giocatore(Socket socketClient)
    {
        this.socketDelGiocatore = socketClient;
        this.manoGiocatore = new ManoGiocatore();
    }

    //get socket
    public Socket getSocket()
    {
        return this.socketDelGiocatore;
    }

    //get mano giocatore
    public ManoGiocatore getManoGiocatore()
    {
        return this.manoGiocatore;
    }

}
