//////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE GESTIONEGIOCATORI CONTENENTE LA LISTA DEI GIOCATORI                                        //
//UTILITA': GESTIONE DEI GIOCATORI CHE PARTECIPERANNO AL GIOCO, RICONOSCIMENTO CLIENT DATE LE SOCKET//
//////////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.Socket;
import java.util.*;

public class GestioneGiocatori {
    //lista giocatori
    private List<Giocatore> listaGiocatori;

    //costruttore di default
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
            if(this.listaGiocatori.get(i).getSocket().equals(tmpSocket))
                return i;
        }
        return -1;
    }

    //Metodo da usare nel gioco per attivare e disattivare il turno di ogni giocatore alla fine prima di chiudere la connessione
    //set turno giocatore a true o false
    public void setTurnoGiocatore(Socket sClientTemp, boolean turno) {
        for (int i = 0; i < this.listaGiocatori.size(); i++) {
            if (this.listaGiocatori.get(i).getSocket().equals(sClientTemp)) 
                this.listaGiocatori.get(i).setUrTurn(turno);
        }
    }

    //metodo utile a restituire un giocatore dalla lista data la sua posizione in essa
    public Giocatore getGiocatore(int posG)
    {
        return this.listaGiocatori.get(posG);
    }
}

