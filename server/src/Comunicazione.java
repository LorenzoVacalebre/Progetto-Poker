import java.io.*;
import java.net.*;

public class Comunicazione {
    private ServerSocket serverSocket;
    private int port;
    private GestioneInfoClients gG;

    public Comunicazione(int port) {
        this.port = port;
    }

    public void avviaServer() {
        try {
            //inzializzazione socket del server
            serverSocket = new ServerSocket(port);

            //mantenimento server in ascolto
            while (true) {
                //la socket del server aspetta fino a quando un client si connette con successo
                Socket clientSocket = serverSocket.accept();

                //crezione di uno stream di input per ricevere dati dal client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //lettura dato inviato dal client
                String clientMessage = in.readLine();

                //metodo controllo quale client ha inviato l'informazione
                gG.checkInfo(clientMessage);

                //chiusura connessione fra client e server
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
