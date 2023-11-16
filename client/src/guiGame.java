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

public class guiGame extends JFrame 
{
    private static int PUNTATA_MASSIMA = 1000;

    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;
    private GridBagConstraints contenitore;
    private JComboBox<String> menuTendina;
    private BufferedImage imgGiocatore;
    private BufferedImage imgDealer;
    private BufferedImage imgcarta;

    public boolean isClose;
    public int puntata;

    public boolean isScommesso;
    public boolean isPassato;
    public boolean isAbbandonato;

    private JButton scommetti;
    private JButton passa;
    private JButton alzaPuntata;

    public comunicazione communication;
    private gioco play;
    private carte carte;
    private carte flop;

    public guiGame(comunicazione communication, carte carte) throws IOException 
    {
        //avvio la comunicazione
        this.communication = communication;

        //classe che gestisce il gioco in generale interfacciandosi anche con la comunicazione
        this.play = new gioco(this);

        //classe che mi permette di partire gestire le carte
        this.carte = carte;

        //valore che mi permette di capire quando cambiare pagina e avviare la comunicazione dal main
        isClose = true;

        //variabile che mi fa capire se il client vuole scommettere
        isScommesso = false;

        //variavile che mi permette di far abbandonare la partita al client
        isAbbandonato = false;

        //valore che poi invio al client relativo alla puntata
        puntata = 0;

        flop = new carte();
        

        //sfondo
        immagineSfondo = ImageIO.read(new File("client/immagini/tavolo.jpg"));
        pannelloSfondo = creaPannelloConSfondo();
        contenitore = new GridBagConstraints();
        pannelloSfondo.setLayout(new GridBagLayout());

        //bottone scommetti
        scommetti = new JButton("Scommetti");
        scommetti.setPreferredSize(new Dimension(200, 50));
        scommetti.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addComponent(500, 1200, 0, 0, scommetti);
        scommetti.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)  
            {
                try {
                    play.scommetti();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        //bottone passa
        passa = new JButton("Passa");
        passa.setPreferredSize(new Dimension(200, 50));
        passa.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addComponent(700, 1200, 0, 0, passa);
        passa.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)  
            {
                play.passa();         
            }
        });

        //bottone alza la puntata
        alzaPuntata = new JButton("Avanti");
        alzaPuntata.setPreferredSize(new Dimension(200, 50));
        alzaPuntata.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addComponent(600, 1200, 0, 0, alzaPuntata);
        alzaPuntata.addActionListener(new ActionListener() 
        {
           @Override
            public void actionPerformed(ActionEvent e)  
            {
                try {
                    play.avanti();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        
        //menu a tendina
        String[] opzioniMenu = {"Menù Funzionalità", "Regolamento", "Abbandona partita"};
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
                        leftGame();
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
        imgDealer = resizeImage(imgDealer, 289, 200); 
        this.addComponent(0, 40, 630, 0, new JLabel(new ImageIcon(imgDealer)));

        //impostazioni di default
        setTitle("Casino.com");
        add(pannelloSfondo);
        setSize(1500, 900);
        //chiudo la finestra e chiudo anche il "programma"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //immagine delle carte
        this.mostraCarteIniziali();
        
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

    public void leftGame() throws IOException, URISyntaxException 
    {
        if (menuTendina.getSelectedItem().equals("Abbandona partita"))
        {
            setVisible(false);
            communication.output("abbandonaPartita");
            communication.terminateConnection();
        } 
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

    public void inserisciErrore(String message, String title) 
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    //metodo per visualizzare nel modo corretto la mano del giocatore
    public void mostraCarteIniziali() throws IOException
    {
        //percorso file della carta
        String percorsoCarta = "";

        //scorro la mano
        for(int i= 0; i<this.carte.size(); i++)
        {
            //salvo il percorso file per ottenere l'immagine corretta 
            percorsoCarta = carte.lista.get(i).getNumero() + carte.lista.get(i).getSeme() + ".png";

            //trovo l'immagine dalla giusta cartella
            imgcarta = ImageIO.read(new File("client/immagini/carte/" + percorsoCarta));

            //imposto la corretta dimensione
            imgcarta = resizeImage(imgcarta, 80, 100);
            
            //inserimento nel modo corretto delle carte nelle apposite label
            if(i == 1)
                this.addComponent(250, 0, 0, 100, new JLabel(new ImageIcon(imgcarta)));
            else
                this.addComponent(250, 100, 0, 0, new JLabel(new ImageIcon(imgcarta)));
        }
    }

    public void mostraFlop() throws IOException
    {
        String linea = this.communication.input();
        String[] vett = linea.split("/");

        for (int i = 1; i <= 3; i++) {
            String[] vettCarte = vett[i].split(";");

            carta c = new carta(vettCarte[0], vettCarte[1], true);
            this.flop.addCarta(c);
        }

        String percorsoCarta = "client/immagini/carte/";

        for (int i = 0; i < this.flop.size(); i++) {
            carta currentCarta = this.flop.lista.get(i);

            String percorsoCompleto = percorsoCarta + currentCarta.getNumero() + currentCarta.getSeme() + ".png";

            try {
                BufferedImage imgCarta = ImageIO.read(new File(percorsoCompleto));

                imgCarta = resizeImage(imgCarta, 80, 100);

                JLabel labelCarta = new JLabel(new ImageIcon(imgCarta));
                if (i == 1)
                    this.addComponent(0, 0, 0, 200, labelCarta);
                else if (i == 2)
                    this.addComponent(0, 0, 0, 0, labelCarta);
                else
                    this.addComponent(0, 200, 0, 0, labelCarta);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

