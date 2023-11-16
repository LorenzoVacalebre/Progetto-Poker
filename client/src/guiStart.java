import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private BufferedImage imgCasino;
    public comunicazione communication;
    public guiGame game;

    public guiStart() throws IOException 
    {
        
        //sfondo
        immagineSfondo = ImageIO.read(new File("client/immagini/sfondoStart.jpg"));
        pannelloSfondo = creaPannelloConSfondo();
        contenitore = new GridBagConstraints();
        pannelloSfondo.setLayout(new GridBagLayout());

        //bottone di inizio
        start = new JButton("Unisciti alla partita");
        start.setPreferredSize(new Dimension(200, 50));
        start.setFont(new Font("Arial", Font.PLAIN, 20));
        this.addComponent(770, 20, 0, 0, start);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //creo un oggetto per comunicare
                    communication = new comunicazione();
                    communication.output("client1");
        
                    //linea utile per leggera l'input
                    String linea;

                    //vettore utile a contenere la linea ricevuta splittata
                    String vettore[];

                    //creazione oggetto mano del giocatore
                    carte lista = new carte();
        
                    //inserimento carte nella mano del giocatore
                    for (int i = 0; i < 2; i++) {
                        //ricevo in input la linea
                        linea = communication.input();

                        //stampo la carta ricevuta(non utile)
                        System.out.println(linea);

                        //splitto il vettore
                        vettore = linea.split(";");

                        //creo oggetto carta
                        carta c;
        
                        //se il vettore viene splittato nel modo corretto
                        if (vettore.length >= 3) {
                            //se la carta è scoperta
                            if (vettore[2].equals("true")) {
                                //inizializzo l'oggetto carta con le informazioni utili
                                c = new carta(vettore[0], vettore[1], true);
                            } else {
                                //inizializzo l'oggetto carta con le informazioni utili
                                c = new carta(vettore[0], vettore[1], false);
                            }
        
                            //aggiunta della carta nella mano del giocatore
                            lista.addCarta(c);
                        } else {
                            System.err.println("Formato di input non valido dal server.");
                        }
                    }
        
                    //creo la partita
                    game = new guiGame(communication, lista);
                    
                    //nascondo la finestra di avvio partita
                    setVisible(false);

                    //inizio il gioco
                    game.isClose = false;

                    //visualizzo la finestra del gioco con le carte
                    game.setVisible(true);
        
                } catch (IOException e1) {
                    // Gestisci l'eccezione in modo appropriato, ad esempio, mostrando un messaggio di errore.
                    e1.printStackTrace();
                }
            }
        });

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

