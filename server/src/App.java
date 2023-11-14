public class App {
    public static void main(String[] args) throws Exception {

        int port = 666;
        Comunicazione communication = new Comunicazione(port);
        communication.avviaServer();
    }
}
