public class CartaAsesino extends Carta {
    private static final String ANSI_NEGRO = "\u001B[90m"; 

    // Constructor
    public CartaAsesino(String palabra) {
        super(palabra, 4); 
    }

    @Override
    protected String obtenerColorANSI() {
        return ANSI_NEGRO;
    }

    @Override
    public String revelar() {
        this.revelada = true;
        return "Fin del juego! Has revelado al asesino. Tu equipo pierde inmediatamente.";
    } 
}