import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class guiStart extends JFrame {
    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;
    private GridBagConstraints contenitore;
    private JButton start;
    private JComboBox<String> menuTendina;
    private BufferedImage imgGiocatore;


    public guiStart() throws IOException 
    {
        //sfondo
        immagineSfondo = ImageIO.read(new File("client/immagini/tavolo.jpg"));
        pannelloSfondo = creaPannelloConSfondo();
        contenitore = new GridBagConstraints();
        pannelloSfondo.setLayout(new GridBagLayout());

        //bottone di inizio
        start = new JButton("Inizia la partita");
        start.setPreferredSize(new Dimension(200, 50));
        start.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addComponent(650, 20, 0, 0, start);

        //menu a tendina
        String[] opzioniMenu = {"Menù Funzionalità", "Regolamento"};
        menuTendina = new JComboBox<>(opzioniMenu);
        menuTendina.setPreferredSize(new Dimension(200, 40));
        menuTendina.setFont(new Font("Arial", Font.PLAIN, 17));
        this.addComponent(0, 1200, 700, 0, menuTendina);

            //regolamento
            menuTendina.addActionListener(new ActionListener()
            //anonymous inner class: serve per creare una classe "leggera" o allungare una classe gia esistente
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    try {
                        actionRules();
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        
        //imgGiocatore = ImageIO.read(new File("client/immagini/postazioneGiocatore.jpg"));
        //this.addComponent(0, 100, 0, 0, new JLabel(new ImageIcon(imgGiocatore)));
        //this.addComponent(200, 0, 0, 0, new JLabel(new ImageIcon(imgGiocatore)));
        //this.addComponent(0, 0, 200, 0, new JLabel(new ImageIcon(imgGiocatore)));
        //this.addComponent(0, 0, 0, 100, new JLabel(new ImageIcon(imgGiocatore)));


        //impostazioni di default
        setTitle("Poker.com");
        add(pannelloSfondo);
        setSize(1500, 900);
        //chiudo la finestra e chiudo anche il "programma"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //metodo che mi permette di posizionare qualsiasi componente e di posizionarlo
    private void addComponent(int sx, int up, int dx, int down, JComponent component) 
    {
        contenitore.gridx = 0;
        contenitore.gridy = 1;
        //distanze da i margini
        contenitore.insets = new Insets(sx, up, dx, down);
        pannelloSfondo.add(component, contenitore);
    }

    private JPanel creaPannelloConSfondo() 
    {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                disegnaSfondo(g);
            }
        };
    }

    private void disegnaSfondo(Graphics g) 
    {
        if (immagineSfondo != null)
            g.drawImage(immagineSfondo, 0, 0, getWidth(), getHeight(), this);
    }

    private void actionRules() throws IOException, URISyntaxException 
    {
        if ("Regolamento".equals(menuTendina.getSelectedItem())) 
            exploreUrl("https://poker.md/it/how-to-play-poker/");
    }

    private void exploreUrl(String url) throws IOException, URISyntaxException 
    {
        Desktop.getDesktop().browse(new URI(url));
    }

}
