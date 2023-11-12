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
            
            communication.output("avvia,1");
        }
       
    }

    
}
