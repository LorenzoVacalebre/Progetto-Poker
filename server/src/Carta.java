public class Carta {
    private String numero;
    private String seme;
    private boolean isFaceUp;
    
    public Carta(String numero, String seme)
    {
        this.numero = numero;
        this.seme = seme;
        this.isFaceUp = false;
    }

    //metodo utile a vari controlli di implementazione
    public void visCarta()
    {
        System.out.println(this.numero + " ");
        System.out.println(this.seme + " ");
        System.out.println(this.isFaceUp + " ");
    }

}
