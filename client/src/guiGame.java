import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class guiGame extends JFrame 
{

    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;
    private GridBagConstraints contenitore;
    private JComboBox<String> menuTendina;
    private BufferedImage imgGiocatore;
    private BufferedImage imgDealer;
    private BufferedImage imgcarta;
    private BufferedImage img20;
    private BufferedImage img50;
    private BufferedImage img100;

    private JLabel venti;
    private JLabel cinquanta;
    private JLabel cento;

    private JLabel ventiClick;
    private JLabel cinquantaClick;
    private JLabel centoClick;
  
    public boolean isClose;
    public int puntata;

    public boolean isScommesso;
    public boolean isPassato;
    public boolean isAbbandonato;
    public boolean isOver;

    private JButton scommetti;
    private JButton passa;

    private Font fontTesto;

    public comunicazione communication;
    public gioco play;
    public carte carte;
    public carte flop;

    /***
     * COSTRUTTORE CHE MI PERMETTE DI GESTIRE TUTTA LA GRAFICA DEL GIOCO E IL SUO FUNZIONAMENTO A LIVELLO GRAFICO
     * @param communication COLLEGAMENTO ALLA CONNESSIONE CON IL SERVER
     * @param carte LE CARTE PASSATE SARANNO INIZIALMENTE QUELLE DELLA MANO INIZIALE 
     * @throws IOException
     */
    public guiGame(comunicazione communication, carte carte, carte flop) throws IOException 
    {
        //avvio la comunicazione
        this.communication = communication;

        //classe che gestisce il gioco in generale interfacciandosi anche con la comunicazione
        this.play = new gioco(this);

        //classe che mi permette di partire gestire le carte 
        this.carte = carte;

        //lista con dentro le carte del flop
        this.flop = flop;

        //valore che mi permette di capire quando cambiare pagina e avviare la comunicazione dal main
        isClose = true;

        //variabile che mi fa capire se il client vuole scommettere
        isScommesso = false;

        //variavile che mi permette di far abbandonare la partita al client
        isAbbandonato = false;

        //variabile che se impostata a true non posso andare avanti perche ho gia cliccato il pulsante
        isOver = false;

        //valore che poi invio al client relativo alla puntata
        puntata = 0;

        //setto il font delle scritte
        fontTesto = new Font("Arial", Font.PLAIN, 20);

        // sfondo
        try {
            immagineSfondo = ImageIO.read(new File("client/immagini/tavolo.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pannelloSfondo = creaPannelloConSfondo();
        contenitore = new GridBagConstraints();
        pannelloSfondo.setLayout(new GridBagLayout());

        //bottone scommetti
        scommetti = new JButton("Scommetti");
        scommetti.setPreferredSize(new Dimension(200, 50));
        scommetti.setFont(fontTesto);
        this.addComponent(500, 1200, 0, 0, scommetti);
        scommetti.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)  
            {
                try {
                    if(play.isYourTurn)
                    {
                        play.scommetti(puntata);
                        play.riceviTurno();
                        play.riceviTurno();
                        puntata = 0;

                    }  
                    else 
                    {
                        inserisciErrore("NON E' IL TUO TURNO", "NON ENTRA");
                        play.riceviTurno();
                        return;
                    }

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        //bottone passa
        passa = new JButton("Passa");
        passa.setPreferredSize(new Dimension(200, 50));
        passa.setFont(fontTesto);
        this.addComponent(600, 1200, 0, 0, passa);
        passa.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)  
            {
                try {
                    if(play.isYourTurn)
                    {
                        play.passa();
                        play.riceviTurno();
                        play.riceviTurno();

                    }  
                    else 
                    {
                        inserisciErrore("NON E' IL TUO TURNO", "NON ENTRA");
                        play.riceviTurno();
                        return;
                    }

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
        menuTendina.setFont(fontTesto);
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
        
        try {
            // immagine del giocatore
            imgGiocatore = ImageIO.read(new File("client/immagini/imgGiocatore.png"));
            imgGiocatore = resizeImage(imgGiocatore, 70, 70);
            this.addComponent(60, 1000, 0, 0, new JLabel(new ImageIcon(imgGiocatore)));
            this.addComponent(620, 20, 0, 0, new JLabel(new ImageIcon(imgGiocatore)));
            this.addComponent(60, 0, 0, 970, new JLabel(new ImageIcon(imgGiocatore)));

            //immagini delle fish
            img20 = ImageIO.read(new File("client/immagini/20.png"));
            img50 = ImageIO.read(new File("client/immagini/50.png"));
            img100 = ImageIO.read(new File("client/immagini/100.png"));

            img20 = resizeImage(img20, 70, 70);
            img50 = resizeImage(img50, 110, 110);
            img100 = resizeImage(img100, 75, 75);

            ventiClick = new JLabel(new ImageIcon(img20));
            cinquantaClick = new JLabel(new ImageIcon(img50));
            centoClick = new JLabel(new ImageIcon(img100));

            cinquantaClick.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    if(play.cinquanta>0)
                    {
                        puntata+=50;
                        play.cinquanta--;
                        aggiornaFish();
                    }
                    else
                        inserisciErrore("Hai finito queste fish", "Errore");

                }
            });

            centoClick.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    if(play.cento>0)
                    {
                        puntata+=100;
                        play.cento--;
                        aggiornaFish();
                    }
                    else
                        inserisciErrore("Hai finito queste fish", "Errore");

                }
            });

            ventiClick.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    if(play.venti>0)
                    {
                        puntata+=20;
                        play.venti--;
                        aggiornaFish();
                    }
                    else
                        inserisciErrore("Hai finito queste fish", "Errore");

                }
            });

            aggiornaFish();

            this.addComponent(300, 1000, 0, 0, ventiClick);
            this.addComponent(300, 1200, 0, 0, cinquantaClick);
            this.addComponent(300, 1400, 0, 0, centoClick);

            // immagine del dealer
            imgDealer = ImageIO.read(new File("client/immagini/luigi.png"));
            imgDealer = resizeImage(imgDealer, 289, 200);
            
            this.addComponent(0, 40, 630, 0, new JLabel(new ImageIcon(imgDealer)));
            
        } catch (IOException e) {
            // gestione dell'eccezione durante la lettura dell'immagine
            e.printStackTrace();
        }

        // impostazioni di default
        setTitle("Casino.com");
        add(pannelloSfondo);
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mostraManoIniziale();
        this.mostraFlopIniziale();        
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

    private void leftGame() throws IOException, URISyntaxException 
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

    public void inserisciMex(String message, String title) 
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }


    //metodo per visualizzare nel modo corretto la mano del giocatore
    public void mostraManoIniziale() throws IOException
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

    public void mostraFlopIniziale() throws IOException
    {
        String percorsoCarta = "client/immagini/carte/";

        for (int i = 0; i < this.flop.size(); i++) {
            carta currentCarta = this.flop.lista.get(i);

            String percorsoCompleto = percorsoCarta + currentCarta.getNumero() + currentCarta.getSeme() + ".png";

            try {
                BufferedImage imgCarta = ImageIO.read(new File(percorsoCompleto));

                imgCarta = resizeImage(imgCarta, 80, 100);

                JLabel labelCarta = new JLabel(new ImageIcon(imgCarta));
                if (i == 1)
                    this.addComponent(0, 0, 0, 100, labelCarta);
                else if (i == 2)
                    this.addComponent(0, 100, 0, 0, labelCarta);
                else if (i == 3)
                    this.addComponent(0, 300, 0, 0, labelCarta);
                else
                    this.addComponent(0, 0, 0, 300, labelCarta);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void aggiornaFish()
    {
        

        venti.setForeground(Color.WHITE);
        cinquanta.setForeground(Color.WHITE);
        cento.setForeground(Color.WHITE);

        venti.setFont(fontTesto);
        cinquanta.setFont(fontTesto);
        cento.setFont(fontTesto);

        this.addComponent(0, 1200, 300, 0, venti);
        this.addComponent(0, 1200, 220, 0, cinquanta);
        this.addComponent(0, 1200, 140, 0, cento);
    }
}

