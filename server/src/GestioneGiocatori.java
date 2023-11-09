import java.net.Socket;
import java.util.*;

public class GestioneGiocatori {
    private List<Giocatore> listaGiocatori;

    public GestioneGiocatori()
    {
        this.listaGiocatori = new ArrayList<Giocatore>();
    }

    //metodo per aggiungere un nuovo giocatore alla lista
    public boolean push(Giocatore giocatore)
    {
        if(this.listaGiocatori.size() < 3) { this.listaGiocatori.add(giocatore); return false; }
        else return false;
    }

    //metodo per ottenere un determinato giocatore
    public int trovaClient(Socket tmpSocket)
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            if(this.listaGiocatori.get(i).getSocket() == tmpSocket)
                return i;
        }
        return -1;
    }
}

