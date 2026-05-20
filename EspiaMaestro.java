import java.util.Scanner;

public class EspiaMaestro extends Jugador {

    public EspiaMaestro(String nombre) {
        super(nombre);
    }

    public void revelarPatronInicio(Tablero tablero, Scanner scanner) {
        System.out.println("\n=======================================================");
        System.out.println("  PANTALLA SECRETA PARA EL ESPÍA MAESTRO " + equipo + ": " + nombre.toUpperCase());
        System.out.println("=======================================================");
        System.out.println("Los demás jugadores deben apartar la mirada ahora.\n");
        
        mostrarPatronSecreto(tablero);

        System.out.println("Toma una foto o memoriza el patrón.");
        System.out.print("Presiona ENTER cuando estés listo... ");
        scanner.next();
        limpiarConsola();
    }

    private void mostrarPatronSecreto(Tablero tablero) {
        for (int fila = 0; fila < 5; fila++) {
            for (int linea = 1; linea <= 3; linea++) {
                for (int col = 0; col < 5; col++) {
                    Carta carta = tablero.getCarta(fila, col);
                    boolean estadoOriginal = carta.isRevelada();
                    
                    carta.revelada = true; 
                    System.out.print(carta.rellenarCarta(linea) + " ");
                    carta.revelada = estadoOriginal;
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
        System.out.println("=== EL JUEGO HA COMENZADO. EL REVERSO SECRETO HA SIDO OCULTADO ===\n");
    }
}