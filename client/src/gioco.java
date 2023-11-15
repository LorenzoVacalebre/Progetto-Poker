import java.io.IOException;

public class gioco 
{
    public guiGame game;
    private boolean isYourTurn; 
    public carte lista = new carte();


    public gioco(guiGame game)
    {
        this.game = game;
        isYourTurn = false;
        lista = new carte();
    }

    //metodo che ritorna le carte iniziali 
    public void riceviCarteIniziali()
    {
        try 
        {
            //creo la partita solo se il server mi autorizza
            String linea;
            String vettore[];

            for(int i = 0; i < 2; i++)
            {
                linea = game.communication.input();
                System.out.println(linea);
                vettore = linea.split(";");
                carta c;

                if(vettore[2].equals("true"))
                    c = new carta(vettore[0], vettore[1], true);
                else
                    c = new carta(vettore[0], vettore[1], false);
                
                lista.addCarta(c);
            } 

            
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public carte mostraCarteIniziali()
    {
        return this.lista;
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
