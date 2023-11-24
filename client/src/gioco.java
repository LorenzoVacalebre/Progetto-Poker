import java.io.IOException;

public class gioco 
{
    public guiGame game; 
    public boolean isYourTurn;
    public boolean haiVinto;
    public int venti;
    public int cinquanta;
    public int cento;
    
    //costruttore con parametri
    public gioco(guiGame game)
    {
        this.game = game;
        this.isYourTurn = false;
        this.haiVinto = false;
        this.venti = 15;
        this.cinquanta = 10;
        this.cento = 5;
    }

    //metodo che mi permette di scommettere
    public void scommetti(int puntata) throws IOException
    {
        //se ho gia passato non permetto di scommetere in quanto si è gia passato 
        //il turno ed evito anche di mandare continui messaggi al server
        if(!game.isPassato && !game.isScommesso)
        {
            System.out.println("scommetto");
            game.communication.output("scommetti/" + puntata);
            game.play.aspettaInformazioniDalServer();
            game.isScommesso = true; 

            //aggiorna valore della puntata
            game.aggiornaFish(); 
        }
        else
            game.inserisciErrore("Non puoi scommettere se hai passato!", "Errore");
    }

    //metodo che mi permette di passare 
    public void passa() throws IOException
    {
        //se ho passato non posso scommettere ne ripassare 
        if(!game.isScommesso && !game.isPassato)
        {
            System.out.println("passo");
            game.communication.output("passa/0");
            game.play.aspettaInformazioniDalServer();
            game.isPassato = true;  
        }
        else
            game.inserisciErrore("Non puoi passare se hai scommesso!", "Errore");
    }

    //controllo se è il mio turno
    public void aspettaInformazioniDalServer() throws IOException
    {
        String messRicevuto = game.communication.input();
        System.out.println(messRicevuto);

        //se il messaggio è true vuol dire che è il tuo turno e puoi giocare
        if(messRicevuto.equals("true"))
        {
            this.isYourTurn = true;
            return;
        }
        else if(messRicevuto.equals("false"))
        {
            this.isYourTurn = false;
            return;
        }

        //se si riceve la somma di vittoria
        String[] tmp = messRicevuto.split("/");
        if(tmp[2].equals("true"))
            game.inserisciMex("HAI VINTO " + tmp[1] + " COIN", "HAI VINTO!!!");
        else
            game.inserisciMex("HAI PERSO, SCARSO", "HAI PERSO!");

    }

    //metodo utile a svuotare le carte per ricevere quelle nuove
    public void svuotaCarteTurno()
    {
        game.listaCarteGiocatore.svuotaCarte();
        game.flop.svuotaCarte();
    }
}