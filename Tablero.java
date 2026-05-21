import java.util.Random;

public class Tablero {
    private Carta[][] cuadricula;
    private static final int tam = 5;

    public Tablero(String[] diccionario) {
        this.cuadricula = new Carta[tam][tam];
        inicializarTablero(diccionario);
    }

    private void inicializarTablero(String[] diccionario) {
        Random random = new Random();

        String[] palabrasMezcladas = new String[diccionario.length];
        for (int i = 0; i < diccionario.length; i++) {
            palabrasMezcladas[i] = diccionario[i];
        }

        for (int i = palabrasMezcladas.length - 1; i > 0; i--) {
            int indiceAleatorio = random.nextInt(i + 1);
            String temporal = palabrasMezcladas[i];
            palabrasMezcladas[i] = palabrasMezcladas[indiceAleatorio];
            palabrasMezcladas[indiceAleatorio] = temporal;
        }

        //9 Rojas (1), 8 Azules (2), 7 Civiles (3), 1 Asesino (4)
        int[] tipos = new int[25];
        int indiceTipo = 0;
        for (int i = 0; i < 9; i++)  tipos[indiceTipo++] = 1;
        for (int i = 0; i < 8; i++)  tipos[indiceTipo++] = 2;
        for (int i = 0; i < 7; i++)  tipos[indiceTipo++] = 3;
        tipos[indiceTipo] = 4;

        for (int i = tipos.length - 1; i > 0; i--) {
            int indiceAleatorio = random.nextInt(i + 1);
            int temporal = tipos[i];
            tipos[i] = tipos[indiceAleatorio];
            tipos[indiceAleatorio] = temporal;
        }

        int contador = 0;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                String palabraAleatoria = palabrasMezcladas[contador];
                int tipoAsignado = tipos[contador];

                if (tipoAsignado == 4) {
                    this.cuadricula[i][j] = new CartaAsesino(palabraAleatoria);
                } else {
                    this.cuadricula[i][j] = new Carta(palabraAleatoria, tipoAsignado);
                }
                contador++;
            }
        }
    }

    public void mostrarTablero() {
        for (int filaTablero = 0; filaTablero < tam; filaTablero++) {
            for (int lineaCarta = 1; lineaCarta <= 3; lineaCarta++) {
                for (int colTablero = 0; colTablero < tam; colTablero++) {
                    Carta cartaActual = cuadricula[filaTablero][colTablero];
                    System.out.print(cartaActual.rellenarCarta(lineaCarta) + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public String seleccionarCarta(int fila, int columna) {
        if (fila >= 0 && fila < tam && columna >= 0 && columna < tam) {
            Carta carta = cuadricula[fila][columna];
            if (!carta.isRevelada()) {
                return carta.revelar();
            } else {
                return "Esa carta ya ha sido revelada previamente.";
            }
        }
        return "Coordenadas fuera de rango.";
    }

    public Carta getCarta(int fila, int columna) {
        return cuadricula[fila][columna];
    }
}