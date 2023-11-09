public class Gioco {
    private GestioneGiocatori listaGiocatori;
    private int giocatoreEffRic;
    private String funzioneRichiesta;

    public Gioco(GestioneGiocatori listaCompleta)
    {
        this.listaGiocatori = listaCompleta;
    }

    public void setGiocatoreEffRic(int nG)
    {
        this.giocatoreEffRic = nG;
    }

    public void setFunzioneRichiesta(String fR)
    {
        this.funzioneRichiesta = fR;
    }

    public void run()
    {

    }

}
