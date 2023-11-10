import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {
    private List<Carta> mazzo;

    public Mazzo()
    {
        this.mazzo = new ArrayList<Carta>();
    }

    public void riempiMazzo()
    {
        //svutamento mazzo per maggiore sicurezza
        mazzo.clear();

        //vettori contenenti le informazioni utili
        String[] semi = {"Cuori", "Quadri", "Fiori", "Picche"};
        String[] valori = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Regina", "Re", "Asso"};

        //riempimento mazzo con tutte le carte utili
        for (String seme : semi) {
            for (String valore : valori) {
                mazzo.add(new Carta(valore, seme));
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

    //metodo utile ad estrarre carte dal mazzo
    public void pull()
    {
        if (!mazzo.isEmpty()) 
            mazzo.remove(this.mazzo.size() - 1);
    }

    //metodo utile al controllo del mazzo cioè se vieni implementato correttamente
    public void visualizza()
    {
        for(int i = 0; i < this.mazzo.size(); i++)
            this.mazzo.get(i).visCarta();
    }
}
