import java.util.Scanner;

public class Espia extends Jugador {

    public Espia(String nombre) {
        super(nombre);
    }

    public void adivinarPalabra(Tablero tablero, Scanner scanner) {
        System.out.println("\n--- TURNO DE: " + nombre + " (Espía - Equipo " + equipo + ") ---");
        System.out.print("Introduce la FILA (0 a 4): ");
        int fila = scanner.nextInt();
        System.out.print("Introduce la COLUMNA (0 a 4): ");
        int columna = scanner.nextInt();

        String resultado = tablero.seleccionarCarta(fila, columna);
        System.out.println("\nResultado: " + resultado);
    }
    
}