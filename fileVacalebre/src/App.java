import java.io.IOException;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception 
    {
        finestra gui = new finestra();
        createGui(gui);
    }

    public static void createGui(finestra gui) throws IOException
    {
        SwingUtilities.invokeLater(() -> {
            gui.creaFinestra();
        });
    }
}
