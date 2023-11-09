import java.net.Socket;

public class Giocatore {
    //TODO -> Inserimento attributi necessari per un giocatore; ad esempio la mano (carte)
    private Socket socketDelGiocatore;

    public Giocatore(Socket socketClient)
    {
        this.socketDelGiocatore = socketClient;
    }

    public Socket getSocket()
    {
        return this.socketDelGiocatore;
    }

}
