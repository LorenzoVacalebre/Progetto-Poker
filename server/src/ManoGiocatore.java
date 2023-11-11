////////////////////////////////////////////////////////////////////////
//CLASSE MANOGIOCATORE CONTENENTE LA MANO DI UN GIOCATORE             //
//UTILITA': GESTIONE MANO GIOCATORE(IN BASE ALLE RICHIESTE DEL CLIENT)//
////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;

public class ManoGiocatore {
    //lista contenente mano del giocatore
    private List<Carta> manoGiocatore;

    //costruttore di default
    public ManoGiocatore()
    {
        this.manoGiocatore = new ArrayList<Carta>();
    }

    //metodo utile per inserire una carta nella mano del giocatore
    public void push(Carta carta)
    {
        //inserimento carta nella mano del giocatore
        this.manoGiocatore.add(carta);
    }

    //metodo utile ad estrarre carte dal mazzo // l'oggetto carta conterrà un nuovo oggetto Carta creato quando
    //il client invierà al server la richiesta di scartare una determinata carta (passaggio di numero e seme)
    public void pull(Carta carta)
    {
        //for per scorrere tutta la mano del giocatore
        for(int i = 0; i < this.manoGiocatore.size(); i++)
        {
            //se la carta da scartare richiesta dal client è uguale a una delle carte presenti nella propria mano, eliminarla dal mazzo
            //if utile a capire la posizione nella quale si trova la carta da eliminare
            if(carta == this.manoGiocatore.get(i))
                this.manoGiocatore.remove(i);
        }
    }

    //metodo per restituire la size
    public int size()
    {
        return this.manoGiocatore.size();
    }

    //metodo utile a restituire un oggetto carta
    public Carta get(int p)
    {
        return this.manoGiocatore.get(p);
    }

    //metodo utile a svuotare la propria mano (cambio round)
    public void svuotaMano()
    {
        this.manoGiocatore.clear();
    }

}
