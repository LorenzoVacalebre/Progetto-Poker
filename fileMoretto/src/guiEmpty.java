import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class guiEmpty extends JFrame 
{
    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;

    /**
     * costruttore deve fare 2 cose:
     * -impostare lo sfondo
     * -aggiungere un bottone per iniziare la partita
     */
    public guiEmpty() throws IOException 
    {
        //carico l'immagine di sfondo
        immagineSfondo = ImageIO.read(new File("fileMoretto/immagini/tavolo.jpg"));
        // Crea un pannello personalizzato con sfondo
        pannelloSfondo = creaPannelloConSfondo();
        // Imposta il layout del pannello principale
        pannelloSfondo.setLayout(new BorderLayout());
        //JButton bottone = new JButton("Inizia la partita");
        //pannelloSfondo.add(bottone, BorderLayout.SOUTH);
        add(pannelloSfondo);
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //metodo per creare il pannello personalizzato con sfondo
    private JPanel creaPannelloConSfondo() 
    {
        return new JPanel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                //richiamo il metodo che mi permette di disegnare lo sfondo
                disegnaSfondo(g);
            }
        };
    }

    //metodo per disegnare lo sfondo
    private void disegnaSfondo(Graphics g) 
    {
        if (immagineSfondo != null) 
            g.drawImage(immagineSfondo, 0, 0, getWidth(), getHeight(), this);
    }
}
