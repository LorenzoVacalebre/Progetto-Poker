import java.io.IOException;

public class gioco 
{
    public guiGame game; 

    public gioco(guiGame game)
    {
        this.game = game;
    }

    //metodo che mi permette di scommettere
    public void scommetti()
    {
        String messaggioRicevuto = "";
        try 
        {
            //se ho gia passato non permetto di scommetere in quanto si è gia passato il turno
            if(game.isPassato == false)
            {
                System.out.println("sono nell?if");
                game.communication.output("chiama/null");
                game.isScommesso = true;
                messaggioRicevuto = game.communication.input();
                System.out.println(messaggioRicevuto);
            }
            else
            {
                game.inserisciErrore("Non puoi scommettere se hai passato!", "Errore");
            }
            
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    //metodo che mi permette di passare 
    public void passa()
    {
        try 
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
        catch (IOException e1) 
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }    
    }
}
