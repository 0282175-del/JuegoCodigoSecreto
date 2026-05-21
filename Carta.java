public class Carta {
    protected static final String ANSI_RESET  = "\u001B[0m";
    protected static final String ANSI_ROJO    = "\u001B[31m";
    protected static final String ANSI_AZUL   = "\u001B[34m";
    protected static final String ANSI_AMARILLO = "\u001B[33m"; // Para civiles
    protected static final String BLOQUE      = "\u001B[2588m";
    private String palabra;
    private int tipo; // 1. "rojo", 2. "azul", 3. "amarillo", 4. "negro/asesino"
    protected boolean revelada;
    
    
    public Carta(String palabra, int tipo) {
        this.palabra = palabra;
        this.tipo = tipo;
        this.revelada = false;
    }
    
    protected String obtenerColorANSI() {
        switch (this.tipo) {
            case 1:  
            return ANSI_ROJO;

            case 2:  
            return ANSI_AZUL;
            
            default:
                return ANSI_AMARILLO; // Civiles
        }
    }
    
    public String rellenarCarta(int numeroLinea) {
        if (!revelada) {
            switch (numeroLinea) {
                case 1: return "╔════════╗";
                case 2: return "║ " + palabra + " ║"; 
                case 3: return "╚════════╝";
            }
        } else {
            String color = obtenerColorANSI();
            
            // Le aplicamos el color a cada parte de la estructura para un efecto de loseta completa
            switch (numeroLinea) {
                case 1: return color + "╔════════╗" + ANSI_RESET;
                case 2: return color + "║████████║" + ANSI_RESET;
                case 3: return color + "╚════════╝" + ANSI_RESET;
            }
        }
        return "";
    }

    public String revelar() {
        this.revelada = true;
        if (this.tipo == 1) {
            return "¡Punto para el equipo rojo!";
        } 
        else if (this.tipo == 2) {
            return "¡Punto para el equipo azul!";
        } 
        else {
            return "Has revelado un civil. El turno termina.";
        }
    }

    public String getPalabra() {
        return palabra;
    }    
    
    public int getTipo() { 
        return tipo; 
    }

    public boolean isRevelada() { 
        return revelada; 
    }
}
