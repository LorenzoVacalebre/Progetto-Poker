import java.io.IOException;

public class gioco 
{
    public guiGame game; 

    public gioco(guiGame game)
    {
        this.game = game;
    }

    //metodo che mi permette di scommettere
    public void scommetti() throws IOException
{
        //se ho gia passato non permetto di scommetere in quanto si è gia passato il turno
        if(game.isPassato == false)
        {
            game.communication.output("scommetti/10");
            game.isScommesso = true;

        }
        else
        {
            game.inserisciErrore("Non puoi scommettere se hai passato!", "Errore");
        }
    }


    //metodo che mi permette di passare 
    public void passa()
    {
            //se ho passato non posso scommettere
            if(game.isScommesso == false)
            {
                game.communication.output("passa/null");
                game.isPassato = true;
            }
            else
            {
                game.inserisciErrore("Non puoi passare se hai scommesso!", "Errore");
            }
    }

    public void avanti() throws IOException
    {
        game.communication.output("flop/10");

        game.mostraFlop();
    }
}
