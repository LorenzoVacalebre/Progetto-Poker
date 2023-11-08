public class Gioco {
    //3 giocatori che potranno partecipare al gioco
    private Giocatore g1;
    private Giocatore g2;
    private Giocatore g3;

    //costruttore di default
    public Gioco()
    {
        this.g1 = new Giocatore();
        this.g2 = new Giocatore();
        this.g3 = new Giocatore();
    }

    //metodo utile alla lettura del client per capire chi ha effettuato la richiesta e 
    //per associare la funzionalità richiesta al client corretto
    public boolean readClient(String client, String funzRichiesta)
    {
        if(client == "client1") { g1.setFunzionalitaTemporanea(funzRichiesta); return true; }
        else if(client == "client2") { g2.setFunzionalitaTemporanea(funzRichiesta); return true; }
        else if(client == "client3") { g3.setFunzionalitaTemporanea(funzRichiesta); return true; }
        else return false;
    }

}
