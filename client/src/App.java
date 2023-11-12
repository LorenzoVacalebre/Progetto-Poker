import java.io.IOException;

public class App {
    // Dichiarazione come membri di classe per rendere globali
    private static guiStart start;
    private static guiGame game;
    private static comunicazione communication;

    public static void main(String[] args) throws Exception 
    {
        start = new guiStart();
        game = new guiGame();
        communication = new comunicazione();
        if(game.isClose == false)
        {
            communication.output("avvia;1"); //avvio la partita
            scommettiClient();
            passaClient();
            leftGameClient();
        }
       
    }

    private static void scommettiClient() throws IOException
    {
        if(game.isScommesso == true)
        {
            communication.output("scommetti;" + game.puntata); //scommetto
            game.isScommesso = false;
        }
    }

    private static void passaClient() throws IOException
    {
        if(game.isPassato == true)
        {
            communication.output("passa;"); //comunica di passare la mano
            game.isPassato = false;
        }
    }

    private static void leftGameClient() throws IOException
    {
        if(game.isAbbandonato == true)
        {
            communication.output("abbandona;client1"); //abbandona la partita
            game.isAbbandonato = false;
        }
    }

    
}
