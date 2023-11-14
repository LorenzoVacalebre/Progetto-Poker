//////////////////////////////////////////////////////////////////////////////////////
//CLASSE GIOCO CONTENENTELE INFORMAZIONI PIU' IMPORTANTI PER GESTIRE IL GIOCO IN SE'//
//UTILITA': GESTIRA' BENE O MALE TUTTO IL GIOCO DEL POKER                           //
//////////////////////////////////////////////////////////////////////////////////////

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Gioco {
    //lista giocatori
    private GestioneGiocatori listaGiocatori;

    //informazioni temporanee
    private int posGiocatoreEffRic;
    private String funzioneRichiesta;
    private Socket socketClientTmp;
    private Comunicazione comunicazioneTmp = new Comunicazione(666);

    //mazzi utili al gioco
    private Mazzo mazzoDaGioco, mazzoCarteScartate;
    //carte che verranno posizionate sul tavolo
    private Mazzo flopBanco;

    //stato del gioco TRUE -> gioco in esecuzione // FALSE -> gioco fermo
    private boolean statusGioco = false;
    
    //stato del round TRUE -> i giocatori non devono pescare le due carte
    //FALSE -> i giocatori devono pescare le due carte
    private boolean statusRound = false;

    //scommessa totale partita
    private float scommessaTotale = 0;

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////

    //costruttore utile solamente per ottenere la lista dei giocatori
    public Gioco(GestioneGiocatori listaCompleta) { this.listaGiocatori = listaCompleta; this.flopBanco = new Mazzo(); }

    //metodo utile ad impostare il giocatore temporaneo che ha effettuato una determinata richiesta
    public void setPosGiocatoreEffRic(int nG){ this.posGiocatoreEffRic = nG; }

    //metodo utile a salvare la funzione richiesta dal giocatore temporaneo
    public void setFunzioneRichiesta(String fR){ this.funzioneRichiesta = fR; }

    //metodo utile ad impostare lo stato del gioco a true
    public void setStatusTrue(){ this.statusGioco = true; }

    //metodo utile ad impostare lo stato del gioco a false
    public void setStatusFalse(){ this.statusGioco = false; }

    //metodo utile ad impostare il client del server che ha effettuato una richiesta
    public void setSocketClientTmp(Socket tmp) { this.socketClientTmp = tmp; }

    //metodo utile a restituire il client
    public boolean getStatus() { return this.statusGioco;}

    //metodo utile a ottenere indietro la lista dei giocatori modificata
    public GestioneGiocatori getListaGiocatori(){ return this.listaGiocatori; }

    //ritorna lo stato del round
    public boolean isStatusRound() {return statusRound; }

    //setta lo stato del round a false
    public void setStatusRoundFalse() {this.statusRound = false; }

    //setta lo stato del round a true
    public void setStatusRoundTrue() {this.statusRound = true; }

    //metodo utile a togliere le carte dal mazzo da gioco per aggiungerle al banco
    public void distribuisciFlop(){ this.flopBanco.riempiFlop(this.mazzoDaGioco); }

    //metodo utile a svuotare il flop
    public void svuotaFlop(){ this.flopBanco.svuota(this.mazzoCarteScartate); }

    //metodo utile per capire se il flop è già stato riempito o no
    public int getFlopSize(){ return this.flopBanco.getSize(); }

    //metodo per restituire scommessa totale
    public float getScommessaTot(){ return this.scommessaTotale; }

    //metodo utile ad avviare il gioco
    public void creaMazzi()
    {
        //mazzo utilizzato per giocare
        this.mazzoDaGioco = new Mazzo();
        //riempimento mazzo da gioco
        this.mazzoDaGioco.riempiMazzo();
        //mischiamento mazzo da gioco
        this.mazzoDaGioco.mischiaMazzo();

        //mazzo utilizzato per inserirci le carte scartate
        this.mazzoCarteScartate = new Mazzo();
    }

    //metodo per distribuire le carte a tutti i giocatori
    public Carta distribuisciCarta()
    {
        return this.mazzoDaGioco.pull();
    }

    //metodo utile a ricavare il giocatore a cui bisogna distribuire la carta
    public void trovaGiocatoreEInserisciCartaInMano() throws IOException
    {
        this.listaGiocatori.ottieniGiocatore(this.socketClientTmp).getManoGiocatore().push(this.distribuisciCarta());
        int posCartaInserita = this.listaGiocatori.ottieniGiocatore(this.socketClientTmp).getManoGiocatore().size() - 1;
        Carta tmp = this.listaGiocatori.ottieniGiocatore(this.socketClientTmp).getManoGiocatore().get(posCartaInserita);

        //invio al client temporaneo l'informazione utile
        this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, tmp.getNumero() + ";" + tmp.getSeme() + ";" + tmp.getIsFacedUp());
    }

    //metodo utile a restituire lo stato del round
    public boolean statoRound()
    {
        return this.statusRound;
    }

    //metodo utile a svuotare tutte le mani dopo che il round viene terminato
    public void svuotaMani()
    {
        for(int i = 0; i < this.listaGiocatori.size(); i++)
            this.listaGiocatori.getGiocatore(i).getManoGiocatore().svuotaMano(this.mazzoCarteScartate);
    }

    //metodo per copiare la puntata più alta e inserirla nel client che ha effettuato la richiesta
    private void copiaPuntata() {
        //valori impossibili
        float puntataMassima = -1; 
        int indiceGiocatoreMaxPuntata = -1;
    
        //scorro tutta la lista dei giocatori per trovare la puntata più alta
        for (int i = 0; i < this.listaGiocatori.size(); i++) {
            //se il giocatore attuale è diverso da quello che ha chiesto la chiamata
            if (this.posGiocatoreEffRic != i) {
                //mi salvo la puntata attuale del giocatore i
                float puntataGiocatoreCorrente = this.listaGiocatori.getGiocatore(i).getPuntata();
                //se la puntata del giocatore i è superiore a quella massima (più alta)
                if (puntataGiocatoreCorrente > puntataMassima) {
                    //salvo sia la puntata che l'indice del giocaore che l'ha effettuata
                    puntataMassima = puntataGiocatoreCorrente;
                    indiceGiocatoreMaxPuntata = i;
                }
            }
        }
    
        //se è stata trovata almeno un'altra puntata
        if (indiceGiocatoreMaxPuntata != -1) {
            //prendo la puntata utile
            float puntataDaCopiare = this.listaGiocatori.getGiocatore(indiceGiocatoreMaxPuntata).getPuntata();
            //la copio nella puntata del giocatore che ha effettuato la richiesta
            this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).addPuntata(puntataDaCopiare);
        }
    }

    //metodo per alzare la puntata
    public void aumentaPuntata(String puntataDaAggiungere)
    {
        //sommo alla puntata del giocatore quella nuova
        this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).addPuntata2(puntataDaAggiungere);
    }
    
    //metodo per trasformare il flop in string e mandarlo ai client
    public String flopToString()
    {
        return this.flopBanco.flopToString();
    }

    //metodo aggiungi una carta al flop
    public void aggiungiCartaFlop()
    {
        //inserimento carta nel flop
        this.flopBanco.push(this.distribuisciCarta());
    }

    //metodo utile a fare terminare il gioco
    public void showDown() throws IOException
    {
        //invio a tutti i client che devono scoprire le carte
        this.comunicazioneTmp.inviaInfoATuttiScopriCarte();

        //finisci la partita
        this.statusRound = false;

        //svuota tutto
        this.svuotaMani();
        this.svuotaFlop();

        //invio a tutti che la partita è finita
        this.comunicazioneTmp.inviaInfoATuttiFine();
    }

    //metodo per ordinare la mano del giocatore
    private void ordinaMano(ManoGiocatore mC, String[] comb)
    {
        Collections.sort(mC.mano(), Comparator.comparing(Carta::getNumero));
    }

    //metodo per controllare se è dello stesso seme
    private boolean isSameSeme(List<Carta> mano)
    {
        //salvo primo seme
        String primoSeme = mano.get(0).getSeme();
        //scorro tutta la mano
        for(Carta c: mano)
        {
            //se viene trovato un seme diverso ritorno false
            if(c.getSeme() != primoSeme)
                return false;
        }
        //ritorno true
        return true;
    }

    //metodo per controllare se è consecutivo
    private boolean isConsecutive(List<Carta> mano)
    {
        //scorro mano giocatore
        for (int i = 1; i < mano.size(); i++) {
            //salvo la seconda carta e la precedente
            int current = Integer.parseInt(mano.get(i).getNumero());
            int previous = Integer.parseInt(mano.get(i - 1).getNumero());

            //se non sono in ordine restituisce false
            if (current != previous + 1) {
                return false;
            }
        }
        //restituisce true se sono in ordine
        return true;
    }

        /////////////////////////////////////////////
        ////////////CONTROLLI COMBINAZIONI///////////
/////////////////////////////////////////////////////////////

    //metodo per controllare se viene trovata un scala reale
    private boolean controlloScalaReale(ManoGiocatore mC, String[] comb)
    {
        //ordina la mano in ordine crescente
        this.ordinaMano(mC, comb);

        //controllo scale reale
        if(mC.mano().get(0).getNumero() == "10" &&
            mC.mano().get(1).getNumero() == "11" &&
            mC.mano().get(2).getNumero() == "12" &&
            mC.mano().get(3).getNumero() == "13" &&
            mC.mano().get(4).getNumero() == "14" &&
            this.isSameSeme(mC.mano()) //se è dello stesso seme
        )
            return true;
        return false;
    }

    //metodo per controllare se viene trovata una scala colore
    private boolean controlloScalaColore(ManoGiocatore mC, String[] comb)
    {
        //ordino la mano del giocatore
        this.ordinaMano(mC, comb);

        //se i numeri sono consecutivi e dello stesso seme ritorna true se no false
        if (isConsecutive(mC.mano()) && isSameSeme(mC.mano())) 
            return true;

        //ritorna false non è stata effettuata la scala colore
        return false;
    }

    //metodo per controllare se è stato effettuato un poker
    private boolean controlloPoker(ManoGiocatore mC, String[] comb)
    {
        //ordino la mano del giocatore
        this.ordinaMano(mC, comb);

        //contatore carte uguali
        int contatore = 1;

        //scorrimento mano
        for (int i = 1; i < mC.size(); i++) {
            //se attuale e precedente sono uguali
            if (mC.get(i).getNumero() == mC.get(i - 1).getNumero())
                contatore++;
            else 
                contatore = 1;
    
            //se sono state trovate 4 carte uguali vuol dire che è stato fatto poker
            if (contatore == 4) {
                //return true
                return true;
            }
        }

        //return false
        return false;
    }

    //metodo per controllare se è statoeffettuato un full
    private boolean controlloFull(ManoGiocatore mC, String[] comb) {
        //ordinamento mano giocatore
        this.ordinaMano(mC, comb);
    
        //contatori 
        int contatore1 = 1;
        int contatore2 = 1;
    
        //scorrimento mano
        for (int i = 1; i < mC.size(); i++) {

            //se attuale e precedente sono uguali
            if (mC.get(i).getNumero() == mC.get(i - 1).getNumero()) 
                contatore1++; 
            else 
            {
                //se il primo contatore è uguale a 2
                if (contatore1 == 2) {
                    //coppia trovata
                    contatore2 = 1;
                } 
                else if (contatore1 == 3) 
                {
                    //tris trovato
                    //scorrimento per trovare la coppia
                    for (int j = i - 1; j >= 0; j--) {
                        //se i numeri sono uguali
                        if (mC.get(j).getNumero() == mC.get(i).getNumero()) {
                            contatore2++;
                        } else {
                            break;
                        }
                    }
    
                    //se abbiamo trovato la coppia dopo il tris, poker
                    if (contatore2 == 2) {
                        return true;
                    }
                }
                //primo contatore a 1
                contatore1 = 1;
            }
        }
    
        return false;
    }

    //metodo per controllare se è stato effettuato colore
    private boolean controlloColore(ManoGiocatore mC, String[] comb)
    {
        //se tutta la mano è dello stesso seme ritorno true 
        if(isSameSeme(mC.mano()))
            return true;

        //return false
        return false;
    }

    //metodo per controllare se è stata effettuata scala
    private boolean controlloScala(ManoGiocatore mC, String[] comb)
    {
        //ordinamento mano giocatore
        this.ordinaMano(mC, comb);

        //se la mano è tutta consecutiva cè una scala
        if(isConsecutive(mC.mano()))
            return true;

        //ritorno false
        return false;
    }

    //metodo per controllare se è stato effettuato un tris
    private boolean controlloTris(ManoGiocatore mC, String[] comb)
    {
        //ordinamento mano giocatore
        this.ordinaMano(mC, comb);

        int contatoreTris = 1;

        //scorrimento mano
        for (int i = 1; i < mC.size(); i++) {
            //se attuale e precedente sono uguali
            if (mC.get(i).getNumero() == mC.get(i - 1).getNumero()) 
                contatoreTris++;
            else 
            {
                //se contatore = 3 significa tris
                if (contatoreTris == 3) 
                    return true;
                
                //reset
                contatoreTris = 1;
            }
        }
        //ritorno false
        return false;
    }

    //metodo per controllare se c'è una coppia
    private boolean controlloDoppiaCoppia(ManoGiocatore mC, String[] comb)
    {
        //ordinamento mano giocatore
        this.ordinaMano(mC, comb);

        //contatori mani
        int contatore1 = 1;
        int contatore2 = 1;

        //abbiamo trovato la prima coppia?
        boolean primaCoppiaTrovata = false;

        //scorrimento mano
        for (int i = 1; i < mC.size(); i++) {
            //se attuale e prec sono uguali
            if (mC.get(i).getNumero() == mC.get(i - 1).getNumero()) {
                contatore1++;

                //è ora di trovare la seconda coppia
                if (primaCoppiaTrovata && contatore1 == 2) {
                    contatore2 = 1;
                }
            } 
            else 
            {
                //prima coppia trovata
                if (contatore1 == 2) {
                    primaCoppiaTrovata = true;
                } 
                else if (contatore1 == 3) 
                {
                    //controllo seconda coppia
                    for (int j = i - 1; j >= 0; j--) {
                        // Se i numeri sono uguali
                        if (mC.get(j).getNumero() == mC.get(i).getNumero()) {
                            contatore2++;
                        } else {
                            break;
                        }
                    }

                    //se abbiamo trovato la seconda coppia abbiamo una doppia coppia
                    if (contatore2 == 2) {
                        return true;
                    }
                }

                //reset
                contatore1 = 1;
                contatore2 = 1;
            }
        }

        //ritorno false
        return false;
    }

    //metodo per controllare la presenza di una coppia
    private boolean controlloCoppia(ManoGiocatore mC, String[] comb)
    {
        //ordinamento mano giocatore
        this.ordinaMano(mC, comb);

        //contatore mano
        int contatore1 = 1;

        //scorrimento mano
        for (int i = 1; i < mC.size(); i++) {
            //se attuale e proc sono uguali
            if (mC.get(i).getNumero() == mC.get(i - 1).getNumero())
                contatore1++;
            else
            {
                //se abbiamo trovato una coppia restituisci true
                if (contatore1 == 2) 
                    return true;

                //reset
                contatore1 = 1;
            }
        }

        //ritorno false
        return false;
    }
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////

    //metodo per effettuare i controlli effettivi
    public int settaCombinazione(ManoGiocatore mC, String[] comb)
    {
        if(this.controlloScalaReale(mC, comb) == true)
            return 0;
        if(this.controlloScalaColore(mC, comb) == true)
            return 1;
        if(this.controlloPoker(mC, comb) == true)
            return 2;
        if(this.controlloFull(mC, comb) == true)
            return 3;
        if(this.controlloColore(mC, comb) == true)
            return 4;
        if(this.controlloScala(mC,comb) == true)
            return 5;
        if(this.controlloTris(mC, comb) == true)
            return 6;
        if(this.controlloDoppiaCoppia(mC, comb) == true)
            return 7;
        if(this.controlloCoppia(mC, comb) == true)
            return 8;
        return -1;
    }

    //metodo per trovare la combinazione migliore
    private int getPosTrovaCombinazioneMigliore(String[] combinazioni) {
        int posMigliore = 0;
    
        for (int i = 1; i < this.listaGiocatori.size(); i++) {
            //salvo combinazioni
            String combinazioneAttuale = this.listaGiocatori.getGiocatore(i).getManoGiocatore().getCombinazioneCarte();
            String combinazioneMigliore = this.listaGiocatori.getGiocatore(posMigliore).getManoGiocatore().getCombinazioneCarte();
    
            //confronto combinazioni
            if (trovaPosizioneCombinazione(combinazioni, combinazioneAttuale) < 
                trovaPosizioneCombinazione(combinazioni, combinazioneMigliore)) 
                posMigliore = i;
        }
    
        return posMigliore;
    }
    
    //metodo utile a trovare la posizione della combinazione nell'array
    private int trovaPosizioneCombinazione(String[] combinazioni, String combinazione) {
        for (int i = 0; i < combinazioni.length; i++) {
            if (combinazioni[i].equals(combinazione)) {
                return i;
            }
        }
        return -1; 
    }
    

    //metodo per decretare il vincitore del round
    private Socket trovaVincitore()
    {
        //salvo tutte le combinazioni
        String[] combinazioni = {"scalaReale", "scalaColore", "poker", "full", "colore", "scala", "tris", "dCoppia", "coppia", "alta"};

        //scorro la lista giocatori
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            //metto insieme una mano dato il flop e la mano del giocatore
            ManoGiocatore manoDaControllare = this.listaGiocatori.getGiocatore(i).getManoGiocatore().mettiInsiemeMano(flopBanco);
            //setto la combinazione tramite dei controlli
            int posCombinazione = this.settaCombinazione(manoDaControllare,combinazioni);
            //imposto la combinazione nella mano di ogni giocatore
            this.listaGiocatori.getGiocatore(i).getManoGiocatore().setCombinazioneCarte(combinazioni[posCombinazione]);
        }

        //salvo la posizione del vincitore
        int posVincitore = this.getPosTrovaCombinazioneMigliore(combinazioni);

        //ritorno la socket del vincitore
        return this.listaGiocatori.getGiocatore(posVincitore).getSocket();
    }

    //metodo per assegnare il piatto al giocatore vincente
    public void assegnazionePiatto() throws IOException
    {
        //salvo la socket del vincitore per mandargli la vincita
        Socket socketVincitore = this.trovaVincitore();

        //invio al client temporaneo l'informazione utile, in questo caso viene svuotata la sua mano
        this.comunicazioneTmp.invioInformazioniAlClient(socketVincitore, "vincita/" + this.scommessaTotale);
    }

    //metodo utile a giocare il turno al client che ha effettuato la richiesta al server
    public void eseguiMano() throws IOException
    {
        String[] tmp = this.funzioneRichiesta.split("/");
        //che funzione ha richiesto il client?
        switch(tmp[0])
        {
            //passare il round(non verrà più preso in causa fino alla fine del roundo)
            case "passa":
                //inserimento carte scartate nel mazzo corretto
                this.mazzoCarteScartate.pushMano(this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getManoGiocatore());

                //eliminazione mano del giocatore che ha richiesto la funzione passa
                this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getManoGiocatore().svuotaMano(this.mazzoCarteScartate);

                //invio al client temporaneo l'informazione utile, in questo caso viene svuotata la sua mano
                this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, "svuotaMano");

                //il giocatore non parteciperà più al round
                this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).setStatusPresenza(false);

                break;

            //il client accetta la puntata più alta effettuata e punta lo stesso
            case "chiama":
                //trovo puntata più alta e la copio nel giocatore che sta giocando
                this.copiaPuntata();
                //invio al client temporaneo l'informazione utile, la nuova punta
                this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, "puntata/" + 
                    this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getPuntata());
                
                //aggiunta scommessa giocatore al piatto
                this.scommessaTotale += this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getPuntata();

                break;
            case "alzaPuntata":

                //trovo puntata più alta e la copio nel giocatore che sta giocando
                this.copiaPuntata();

                //alzamento puntata
                this.aumentaPuntata(tmp[1]);

                //aggiunta scommessa giocatore al piatto
                this.scommessaTotale += this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getPuntata();

                //invio al client temporaneo l'informazione utile, in questo caso viene svuotata la sua mano
                this.comunicazioneTmp.invioInformazioniAlClient(this.socketClientTmp, "puntata/" + 
                    this.listaGiocatori.getGiocatore(this.posGiocatoreEffRic).getPuntata());

                break;
            default:
                break;
        }
    }
}
