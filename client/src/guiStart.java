import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class guiStart extends JFrame 
{
    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;
    private GridBagConstraints contenitore;
    private JButton start;

    public guiStart() throws IOException 
    {
        //codice per mettere lo sfondo insieme ai metodi creati sotto per disegnarlo
        immagineSfondo = ImageIO.read(new File("client/immagini/tavolo.jpg"));
        pannelloSfondo = creaPannelloConSfondo();
        contenitore = new GridBagConstraints();
        pannelloSfondo.setLayout(new GridBagLayout());
        
        //codice per aggiungere un pulsante DOVE VOGLIO IO
        start = new JButton("Inizia la partita");
        start.setPreferredSize(new Dimension(200, 50));
        start.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addButton(650, 20 , 0 , 0, start);


        //aggiungere menu a tendina 
        

        //impostazioni di default della finestra
        setTitle("Poker.com");
        add(pannelloSfondo);
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    //metodo che semplifica il posizionamento di pulsanti
    private void addButton(int a, int b, int c, int d, JButton s)
    {
        contenitore.gridx = 0;
        contenitore.gridy = 1;
        contenitore.insets = new Insets(a, b, c, d);
        pannelloSfondo.add(s, contenitore);

    }

    //metodo che mi permette di inserire lo sfondo alla finestra
    private JPanel creaPannelloConSfondo() 
    {
        return new JPanel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                disegnaSfondo(g);
            }
        };
    }

    //metodo che disegna lo sfondo
    private void disegnaSfondo(Graphics g) 
    {
        if (immagineSfondo != null) 
            g.drawImage(immagineSfondo, 0, 0, getWidth(), getHeight(), this);
    }
}
