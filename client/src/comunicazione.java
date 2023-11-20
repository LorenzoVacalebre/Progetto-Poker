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

<<<<<<< HEAD
 public class comunicazione 
 {
=======
 public class comunicazione {
>>>>>>> 153069ef01e1e7b8dfdd1db517f5d18f5f22b3d3
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;

<<<<<<< HEAD
    public comunicazione() throws UnknownHostException, IOException 
    {
=======
    public comunicazione() throws UnknownHostException, IOException {
>>>>>>> 153069ef01e1e7b8dfdd1db517f5d18f5f22b3d3
        this.clientSocket = new Socket("127.0.0.1", 666);
        this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    //metodo utile a ricevere in input un'informazione
<<<<<<< HEAD
    public String input() throws IOException 
    {
        String messaggioLetto = input.readLine();
=======
    public String input() throws IOException {
        //stop per leggere l'input
        String messaggioLetto = input.readLine();
        //se non arriva alcun messaggio
        if (messaggioLetto == null) {
            throw new IOException("Il server ha chiuso la connessione.");
        }
>>>>>>> 153069ef01e1e7b8dfdd1db517f5d18f5f22b3d3
        return messaggioLetto;
    }

    //metodo per inviare informazioni al server
<<<<<<< HEAD
    public void output(String mess) 
    {
=======
    public void output(String mess) {
>>>>>>> 153069ef01e1e7b8dfdd1db517f5d18f5f22b3d3
        output.println(mess);
    }

    //metodo per terminare totalmente la connessione
<<<<<<< HEAD
    public void terminateConnection() throws IOException 
    {
=======
    public void terminateConnection() throws IOException {
>>>>>>> 153069ef01e1e7b8dfdd1db517f5d18f5f22b3d3
        try {
            this.input.close();
            this.output.close();
            this.clientSocket.close();
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            throw e;
        }
    }
}

    

