////////////////////////////////////////////////////////////////
//la divisione delle diverse informazioni avverrà tramite il ";"
//vett[0] = *numClient* / vett[1] = *funzioneRichiesta*
////////////////////////////////////////////////////////////////

public class GestioneInfoClients {
    //vettore che conterrà tutte le informazioni utili ricevute dal client
    private String[] informaziniSplittate;
    //definizione e inzializzazione del gioco in sè
    private Gioco gioco = new Gioco();

    //metodo utile a controllare le informazioni ricevute dai client
    public void checkInfo(String clientMex)
    {
        //salvataggio informazioni 
        informaziniSplittate = clientMex.split(";");

        //controllo quale client ha effettuato una richiesta la server
        if(gioco.readClient(informaziniSplittate[0], informaziniSplittate[1]) == false) return;
    }
}
