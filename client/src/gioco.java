import java.io.IOException;

public class gioco 
{
    public guiGame game; 
    public boolean isYourTurn;
    public boolean haiVinto;
    public int venti;
    public int cinquanta;
    public int cento;
    

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
        //se ho gia passato non permetto di scommetere in quanto si è gia passato il turno ed evito anche di mandare continui messaggi al server
        if(!game.isPassato && !game.isScommesso)
        {
            System.out.println("scommetto");
            game.communication.output("scommetti/" + puntata);
            game.play.riceviTurno();
            game.isScommesso = true; 
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
            game.play.riceviTurno();
            game.isPassato = true;  
            //this.controlloVincita();

        }
        else
            game.inserisciErrore("Non puoi passare se hai scommesso!", "Errore");
    }

    public void controlloVincita() throws IOException
    {
        String messRicevuto = game.communication.input();
        System.out.println(messRicevuto);
        String[] info = messRicevuto.split("/");
        
    }

    //controllo se è il mio turno
    public void riceviTurno() throws IOException
    {
        String messRicevuto = game.communication.input();
        System.out.println(messRicevuto);
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

        String[] tmp = messRicevuto.split("/");
        if(tmp[2].equals("true"))
            game.inserisciMex("HAI VINTO " + tmp[1] + " COIN", "HAI VINTO!!!");
        else
            game.inserisciMex("HAI PERSO, SCARSO", "HAI PERSO!");

    }

    public void svuotaCarteTurno()
    {
        game.carte.svuotaCarte();
        game.flop.svuotaCarte();
    }
}