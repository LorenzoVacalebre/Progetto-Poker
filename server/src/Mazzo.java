///////////////////////////////////////////////////////////////////////////////
//CLASSE MAZZO CONTENENTE LA LISTA DI UN MAZZO(MAZZO DA GIOCO E MAZZO SCARTI)//
//UTILITA': GESTIONE GENERALE MAZZO                                          //
///////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {
    //lista di carte (mazzo)
    private List<Carta> mazzo;

    //costruttore di default
    public Mazzo()
    {
        this.mazzo = new ArrayList<Carta>();
    }

    public void riempiMazzo()
    {
        //svutamento mazzo per maggiore sicurezza e meno problemi
        mazzo.clear();

        //vettori contenenti le informazioni utili alla creazione delle carte
        String[] semi = {"Cuori", "Quadri", "Fiori", "Picche"};
        String[] numeri = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Regina", "Re", "Asso"};

        //riempimento mazzo con tutte le carte utili
        //foreach per scorrere tutti i semi e poi tutti i numeri
        for (String seme : semi) {
            for (String numero : numeri) {
                mazzo.add(new Carta(numero, seme));
            }
        }
    }

    //metodo utile a mischiare il mazzo
    public void mischiaMazzo()
    {
        //mischio il mazzo
        Collections.shuffle(mazzo);
    }

    //metodo utile per inserire una carta al mazzo
    public void push(Carta carta)
    {
        //inserimento carta nel mazzo
        this.mazzo.add(carta);
    }

    //metodo utile per inserire una carta al mazzo
    public void pushMano(ManoGiocatore mano)
    {
        for(int i = 0; i < mano.size(); i++)
            this.mazzo.add(mano.get(i));
    }

    //metodo utile ad estrarre carte dal mazzo
    public Carta pull()
    {
        if (!mazzo.isEmpty()) 
            return mazzo.remove(this.mazzo.size() - 1);
        return null;  
    }

    //metodo utile al controllo del mazzo cioè se vieni implementato correttamente
    public void visualizza()
    {
        for(int i = 0; i < this.mazzo.size(); i++)
            this.mazzo.get(i).visCarta();
    }
}
