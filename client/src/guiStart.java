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

public class guiStart extends JFrame 
{
    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;
    private GridBagConstraints contenitore;
    private JButton start;
    private JComboBox<String> menuTendina;
    private BufferedImage imgGiocatore;
    private BufferedImage imgDealer;
    private BufferedImage imgCasino;
    public comunicazione communication;
    public guiGame game;

    public guiStart() throws IOException 
    {
        
        //sfondo
        immagineSfondo = ImageIO.read(new File("client/immagini/tavolo.jpg"));
        pannelloSfondo = creaPannelloConSfondo();
        contenitore = new GridBagConstraints();
        pannelloSfondo.setLayout(new GridBagLayout());

        //bottone di inizio
        start = new JButton("Unisciti alla partita");
        start.setPreferredSize(new Dimension(200, 50));
        start.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addComponent(770, 20, 0, 0, start);
        start.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    String messaggioRicevuto = "";
                    communication = new comunicazione();
                    communication.output("client1");
                    messaggioRicevuto = communication.input();


                    if(messaggioRicevuto.equals("partitaOnline"))
                    {
                        //creo la partita solo se il server mi autorizza
                        String linea;
                        String vettore[];
                        carte lista = new carte();
                        for(int i = 0; i<2; i++)
                        {
                            linea = communication.input();
                            vettore = linea.split(";");
                            carta c;
                            if(vettore[2].equals("true"))
                                c = new carta(vettore[0], vettore[1], true);
                            else
                                c = new carta(vettore[0], vettore[1], false);
                            lista.addCarta(c);
                        }

                        game = new guiGame(communication, lista);
                        setVisible(false);
                        game.isClose = false;
                        game.setVisible(true);

                    }
                    else
                        communication.output("Errore all'inizio");

                    
                    
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

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
        

        //immagini dei giocatori
        imgGiocatore = ImageIO.read(new File("client/immagini/imgGiocatore.png"));
        imgGiocatore = resizeImage(imgGiocatore, 70, 70); 
        this.addComponent(60, 1000, 0, 0, new JLabel(new ImageIcon(imgGiocatore)));
        this.addComponent(620, 20, 0, 0, new JLabel(new ImageIcon(imgGiocatore)));
        this.addComponent(60, 0, 0, 970, new JLabel(new ImageIcon(imgGiocatore)));

        //immagine del dealer
        imgDealer = ImageIO.read(new File("client/immagini/luigi.png"));
        imgDealer = resizeImage(imgDealer, 255, 200); 
        this.addComponent(0, 40, 630, 0, new JLabel(new ImageIcon(imgDealer)));

        //immagine del casino
        imgCasino = ImageIO.read(new File("client/immagini/scrittaPoker.png"));
        imgCasino = resizeImage(imgCasino, 600, 300); 
        this.addComponent(50, 20, 0, 0, new JLabel(new ImageIcon(imgCasino)));


        //impostazioni di default
        setTitle("Casino.com");
        add(pannelloSfondo);
        setSize(1500, 900);
        //chiudo la finestra e chiudo anche il "programma"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
    }

    //metodo che mi permette di posizionare qualsiasi componente e di posizionarlo
    private void addComponent(int daSu, int daSinistra, int daGiu, int daDestra, JComponent component) 
    {
        contenitore.gridx = 0;
        contenitore.gridy = 1;
        //distanze da i margini
        contenitore.insets = new Insets(daSu, daSinistra, daGiu, daDestra);
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

    //metodo che ridimensiona un'immagine
    private BufferedImage resizeImage(BufferedImage img, int larghezza, int altezza) 
    {
        //creo un'immagine temporanea con le nuove dimensioni
        Image tmp = img.getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);   
        BufferedImage resizedImage = new BufferedImage(larghezza, altezza, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

}

