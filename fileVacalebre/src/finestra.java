import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class finestra 
{
    private static int LARGHEZZA_SFONDO = 900;
    private static int ALTEZZA_SFONDO = 600;

    private JFrame finestra;
    private ImageIcon immSfondo;
    private JLabel sfondo;
    private BufferedImage immagine;

    public finestra() throws IOException 
    {
        finestra = new JFrame("Poker.com");
        immagine = ImageIO.read(new File("immagini/tavolo.jpg"));
        immagine = resize(immagine);
        immSfondo = new ImageIcon(immagine);
        sfondo = new JLabel(immSfondo);

        // Aggiungi un ComponentListener alla finestra
        finestra.addComponentListener(new ComponentAdapter() {
            
            public void componentResized(ComponentEvent e) {
                // Ridimensiona l'immagine quando la finestra cambia dimensione
                immagine = resize(immagine);
                immSfondo.setImage(immagine);
                sfondo.setIcon(immSfondo);
            }
        });
    }

    public void creaFinestra() 
    {
        finestra.add(sfondo);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.pack();
        finestra.setVisible(true);
    }

    private BufferedImage resize(BufferedImage img) 
    {
        Image tmp = img.getScaledInstance(LARGHEZZA_SFONDO, ALTEZZA_SFONDO, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(LARGHEZZA_SFONDO, ALTEZZA_SFONDO, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

}
