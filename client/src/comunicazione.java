import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * classe che mi permette di creare i metodi che mi serviranno per la comunicazione 
 * input = riceve dal server
 * output = invio al server
 * terminateConnection = termina la connessione con il server quando finisce la partita
 */

public class comunicazione 
{
    private String serverAddress; //usiamo per adesso localHost
    private int serverPort; // 666 così sappiamo che sicuramente è utilizzabile
    private Socket clientSocket;

    public comunicazione() throws UnknownHostException, IOException
    {
        this.serverAddress = "127.0.0.1";
        this.serverPort = 666;
        clientSocket = new Socket(this.serverAddress, this.serverPort); 
    }

    public String input() throws IOException
    {
        String messaggioLetto = "";
        //con questa riga di codice apro la comunicazione da server a client, quindi le risposte
        BufferedReader input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        messaggioLetto = input.readLine();
        input.close();
        return messaggioLetto; //recupero i dati dal server
    }

    public void output(String mess) throws IOException
    {
        //con quest'altra posso comunicare tra client e server
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        output.println(mess); //cosi facendo invio dati al server
        output.close();
    }

    public void terminateConnection() throws IOException
    {
        this.clientSocket.close();
    }

    
}