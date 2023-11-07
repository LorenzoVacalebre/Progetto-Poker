import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class finestra 
{
    private static int LARGHEZZA_SFONDO = 1500;
    private static int ALTEZZA_SFONDO = 900;

    private JFrame finestra;
    private ImageIcon immSfondo;
    private JLabel sfondo;
    private BufferedImage immagine;
    private JButton inizia;
    private JButton scommetti;
    private JButton aumenta;
    private JButton passare;
    LayoutManager layout;

    public finestra() throws IOException 
    {
        finestra = new JFrame("Poker.com");
        immagine = ImageIO.read(new File("fileMoretto/immagini/tavolo.jpg"));
        immSfondo = new ImageIcon(immagine);
        sfondo = new JLabel(immSfondo);
        immagine = resize(immagine);
        immSfondo.setImage(immagine);
        sfondo.setIcon(immSfondo);
        inizia = new JButton("Inizia Partita");
        scommetti = new JButton("Scommetti");
        aumenta = new JButton("Aumenta Puntata");
        passare = new JButton("Passa la mano");
        layout = new FlowLayout();
        //finestra.setButton(inizia);
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

    
           /* finestra.setLayout(layout);

            // Aggiungi i bottoni alla finestra
            finestra.add(pulsante1);
            finestra.add(pulsante2);

            finestra.setVisible(true);
            */

}
