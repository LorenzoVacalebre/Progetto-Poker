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
    public boolean aggiungiGiocatore(Giocatore giocatore)
    {
        if(this.listaGiocatori.size() < 3) { this.listaGiocatori.add(giocatore); return true; }
        else return false;
    }

    //metodo per ottenere un determinato giocatore
    public int trovaPosizioneClient(Socket tmpSocket)
    {
        return this.posizioneGiocatore(tmpSocket);
    }

    //metodo per ottenere direttamente il giocatore utile
    public Giocatore ottieniGiocatore(Socket socketClient) {
        for (Giocatore giocatore : this.listaGiocatori) {
            if (giocatore.getSocket().getInetAddress().equals(socketClient.getInetAddress())) {
                return giocatore;
            }
        }
        return null;
    }

    /* 
    //metodo da usare nel gioco per attivare e disattivare il turno di ogni giocatore alla fine prima di chiudere la connessione
    //set turno giocatore a true o false
    public void setTurnoGiocatore(Socket sClientTemp, boolean turno) {
        for (int i = 0; i < this.listaGiocatori.size(); i++) {
            if (this.listaGiocatori.get(i).getSocket().getInetAddress().equals(sClientTemp.getInetAddress())) 
                this.listaGiocatori.get(i).setUrTurn(turno);
        }
    }
    */

    //metodo per controllare se un client già connesso tenta di riconnettersi alla partita
    public boolean controllaDuplicati(Socket sClientTemp)
    {   
        //controllo se il client si è già connesso in precedenza o no
        if(this.posizioneGiocatore(sClientTemp) >= 0)
            return true;
        else 
            return false;
    }

    //metodo per scorrere tutta la lista di giocatori
    private int posizioneGiocatore(Socket sClientTemp)
    {
        //scorrimento lista
        for(int i = 0; i < this.listaGiocatori.size();i++){
            //se client si è già connesso in precedenza
            if(this.listaGiocatori.get(i).getSocket().getInetAddress().equals(sClientTemp.getInetAddress()))
                return i;
        }
        return -1;
    }
    
    //metodo utile a restituire un giocatore dalla lista data la sua posizione in essa
    public Giocatore getGiocatore(int posG)
    {
        return this.listaGiocatori.get(posG);
    }

    //metodo per restituire la grandezza della lista
    public int size() {
        return this.listaGiocatori.size();
    }

    //metodo per eliminare un giocatore dalla partita
    public void pullGiocatore(Socket client)
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            if(client.getInetAddress().equals(this.listaGiocatori.get(i).getSocket().getInetAddress()))
                this.listaGiocatori.remove(i);
        }
    }
}

