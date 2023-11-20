import java.io.IOException;

public class gioco 
{
    public guiGame game; 
    public boolean isYourTurn;
    

    public gioco(guiGame game)
    {
        this.game = game;
        this.isYourTurn = false;
    }

    //metodo che mi permette di scommettere
    public void scommetti() throws IOException
    {
        if(isYourTurn)
        {
            //se ho gia passato non permetto di scommetere in quanto si è gia passato il turno ed evito anche di mandare continui messaggi al server
            if(!game.isPassato && !game.isScommesso)
            {
                System.out.println("scommetto");
                game.communication.output("scommetti/10");
                game.isScommesso = true;

            }
            else
            {
                game.inserisciErrore("Non puoi scommettere se hai passato!", "Errore");
            }
        }
        else
            game.inserisciErrore("Aspetta il tuo turno!", "Errore");
    }


    //metodo che mi permette di passare 
    public void passa()
    {
        if(isYourTurn)
        {
            //se ho passato non posso scommettere ne ripassare 
            if(!game.isScommesso && !game.isPassato)
            {
                System.out.println("passo");
                game.communication.output("passa/null");
                game.isPassato = true;
            }
            else
            {
                game.inserisciErrore("Non puoi passare se hai scommesso!", "Errore");
            }
        }
        else
            game.inserisciErrore("Aspetta il tuo turno!", "Errore");

            
    }

    public void avanti() throws IOException
    {
        if(isYourTurn)
        {
            if(!game.isOver)
            {
                System.out.println("vado avanti");
                game.communication.output("flop/10");
                //game.mostraFlop();
                game.isOver = true;
            }
            else
                game.inserisciErrore("Sei già andato avanti, aspetta il tuo turno", "Errore");
        }
        else
            game.inserisciErrore("Aspetta il tuo turno!", "Errore");
    }

    public void aspettaTurno() throws IOException
    {
        game.communication.output("il client sta aspettando");
        String messaggioLetto = game.communication.input();
        if(messaggioLetto == "true")
            this.isYourTurn = true;
        else 
            this.isYourTurn = false; 
    }
}
