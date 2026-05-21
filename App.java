import java.util.Scanner;
import java.util.Random;

public class App {
    static String[] myPalabras = {
        "AGUILA", "ANILLO", "ASTROS", "AZUCAR", "BALCON", "BANANA", "BARCOS", "BARRIL", "BASURA", "BEBIDA",
        "BODEGA", "BOSQUE", "BRUJAS", "BUTACA", "CABEZA", "CACTUS", "CADENA", "CAJERO", "CALLES", "CAMARA",
        "CAMINO", "CAMPOS", "CEREZA", "CIERVO", "CIUDAD", "COCHES", "COHETE", "CONEJO", "CORONA", "CORREO",
        "CRATER", "CRUCES", "CUERDA", "CUERNO", "CUERPO", "CUPULA", "DIENTE", "DISCOS", "DRAGON", "ESPEJO",
        "ESPINA", "FIESTA", "FLECHA", "FRESAS", "FUEGOS", "FUERZA", "FUTBOL", "GALLOS", "GANADO", "GRANJA",
        "GRIFOS", "GUERRA", "HIERRO", "HUEVOS", "HUESOS", "IMANES", "IMAGEN", "JARDIN", "LADRON", "LANZAS",
        "LENGUA", "LIENZO", "LLUVIA", "LUCERO", "MADERA", "MADRID", "MARCOS", "MEDICO", "MONEDA", "MONJAS",
        "MOSCAS", "MUEBLE", "MUERTE", "MUSEOS", "NAVAJA", "NIEBLA", "OCEANO", "OLIVAS", "PAGINA", "PAJARO",
        "PASADO", "PASTEL", "PATATA", "PELOTA", "PERROS", "PIEDRA", "PILOTO", "PINCEL", "PINTOR", "PIRATA",
        "PLANTA", "PLUMAS", "PREMIO", "PUENTE", "PUERTO", "RAMPAS", "RECETA", "REGALO", "SIRENA", "TEATRO"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("======================================");
        System.out.println("      BIENVENIDO A CÓDIGO SECRETO     ");
        System.out.println("======================================");

        System.out.print("¿Cuántos jugadores van a participar? (mínimo 4): ");
        int numJugadores = scanner.nextInt();
        scanner.nextLine(); 

        Jugador[] jugadores = new Jugador[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            System.out.print("Nombre del jugador " + (i + 1) + ": ");
            String nombre = scanner.nextLine();
            jugadores[i] = new Espia(nombre); 
        }

        Juego[] historialPartidas = new Juego[5];
        int contadorPartidas = 0;

        boolean seguirJugando = true;

        while (seguirJugando && contadorPartidas < 5) {
            System.out.println("\n=======================================");
            System.out.println("        INICIANDO PARTIDA # " + (contadorPartidas + 1));
            System.out.println("=======================================");

            historialPartidas[contadorPartidas] = new Juego(contadorPartidas + 1);

            int[] indicesAleatorios = new int[numJugadores];
            for (int i = 0; i < numJugadores; i++) indicesAleatorios[i] = i;
            
            for (int i = numJugadores - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = indicesAleatorios[i];
                indicesAleatorios[i] = indicesAleatorios[j];
                indicesAleatorios[j] = temp;
            }

            int vanARojo = (numJugadores % 2 == 0) ? (numJugadores / 2) : (numJugadores / 2) + 1;

            for (int i = 0; i < numJugadores; i++) {
                int idx = indicesAleatorios[i];
                String nombreActual = jugadores[idx].getNombre();
                int puntosActuales = jugadores[idx].getJuegosGanados();
                
                jugadores[idx] = new Espia(nombreActual);
                
                for (int p = 0; p < puntosActuales; p++) {
                    jugadores[idx].incrementarGanados();
                }

                if (i < vanARojo) {
                    jugadores[idx].asignarEquipo("ROJO");
                } else {
                    jugadores[idx].asignarEquipo("AZUL");
                }
            }

            int posAleatoriaRoja = random.nextInt(vanARojo);
            int idxMaestroRojo = indicesAleatorios[posAleatoriaRoja];
            
            int posAleatoriaAzul = vanARojo + random.nextInt(numJugadores - vanARojo);
            int idxMaestroAzul = indicesAleatorios[posAleatoriaAzul];

            String nRojo = jugadores[idxMaestroRojo].getNombre();
            int pRojo = jugadores[idxMaestroRojo].getJuegosGanados();
            jugadores[idxMaestroRojo] = new EspiaMaestro(nRojo);
            jugadores[idxMaestroRojo].asignarEquipo("ROJO");
            for (int p = 0; p < pRojo; p++) jugadores[idxMaestroRojo].incrementarGanados();

            String nAzul = jugadores[idxMaestroAzul].getNombre();
            int pAzul = jugadores[idxMaestroAzul].getJuegosGanados();
            jugadores[idxMaestroAzul] = new EspiaMaestro(nAzul);
            jugadores[idxMaestroAzul].asignarEquipo("AZUL");
            for (int p = 0; p < pAzul; p++) jugadores[idxMaestroAzul].incrementarGanados();

            System.out.println("\n=== ROLES Y EQUIPOS ASIGNADOS ===");
            for (Jugador j : jugadores) {
                j.mostrarInformacion(); 
            }

            Tablero tablero = new Tablero(myPalabras);

            for (Jugador j : jugadores) {
                if (j instanceof EspiaMaestro) {
                    ((EspiaMaestro) j).revelarPatronInicio(tablero, scanner);
                }
            }

            String equipoActual = "ROJO";
            boolean juegoTerminado = false;

            while (!juegoTerminado) {
                int cartasRojasRestantes = 0;
                int cartasAzulesRestantes = 0;

                for (int f = 0; f < 5; f++) {
                    for (int c = 0; c < 5; c++) {
                        Carta carta = tablero.getCarta(f, c);
                        if (!carta.isRevelada()) {
                            if (carta.getTipo() == 1) cartasRojasRestantes++;
                            if (carta.getTipo() == 2) cartasAzulesRestantes++;
                        }
                    }
                }

                System.out.println("\n---------------------------------");
                System.out.println("  TURNO DEL EQUIPO: " + equipoActual);
                System.out.println("  [Rojas restantes: " + cartasRojasRestantes + " | Azules restantes: " + cartasAzulesRestantes + "]");
                System.out.println("---------------------------------");
                tablero.mostrarTablero();

                Jugador jugadorTurno = null;
                for (Jugador j : jugadores) {
                    if (j.getEquipo().equals(equipoActual) && j instanceof Espia && !(j instanceof EspiaMaestro)) {
                        jugadorTurno = j;
                        break;
                    }
                }

                if (jugadorTurno == null) {
                    for (Jugador j : jugadores) {
                        if (j.getEquipo().equals(equipoActual)) {
                            jugadorTurno = j;
                            break;
                        }
                    }
                }

                if (jugadorTurno instanceof Espia) {
                    ((Espia) jugadorTurno).adivinarPalabra(tablero, scanner);
                }

            
                for (int f = 0; f < 5 && !juegoTerminado; f++) {
                    for (int c = 0; c < 5; c++) {
                        Carta carta = tablero.getCarta(f, c);
                        
                        if (carta instanceof CartaAsesino && carta.isRevelada()) {
                            
                            System.out.println("\n=================================================");
                            System.out.println("           TABLERO FINAL DE LA PARTIDA           ");
                            System.out.println("=================================================");
                            tablero.mostrarTablero();

                            String equipoGanador = equipoActual.equals("ROJO") ? "AZUL" : "ROJO";
                            System.out.println("¡Victoria absoluta para el equipo " + equipoGanador + "!");
                            
                            historialPartidas[contadorPartidas].registrarGanador(equipoGanador);

                            for (Jugador j : jugadores) {
                                if (j.getEquipo().equals(equipoGanador)) {
                                    j.incrementarGanados();
                                }
                            }
                            juegoTerminado = true;
                            break;
                        }
                    }
                }

                if (!juegoTerminado) {
                    int r = 0, a = 0;
                    for (int f = 0; f < 5; f++) {
                        for (int c = 0; c < 5; c++) {
                            Carta cc = tablero.getCarta(f, c);
                            if (!cc.isRevelada()) {
                                if (cc.getTipo() == 1) r++;
                                if (cc.getTipo() == 2) a++;
                            }
                        }
                    }

                    if (r == 0) {
                        System.out.println("\n¡El equipo ROJO ha ganado la partida!");
                        historialPartidas[contadorPartidas].registrarGanador("ROJO");
                        for (Jugador j : jugadores) {
                            if (j.getEquipo().equals("ROJO")) j.incrementarGanados();
                        }
                        juegoTerminado = true;
                    } else if (a == 0) {
                        System.out.println("\n¡El equipo AZUL ha ganado la partida!");
                        historialPartidas[contadorPartidas].registrarGanador("AZUL");
                        for (Jugador j : jugadores) {
                            if (j.getEquipo().equals("AZUL")) j.incrementarGanados();
                        }
                        juegoTerminado = true;
                    }
                }

                if (!juegoTerminado) {
                    equipoActual = equipoActual.equals("ROJO") ? "AZUL" : "ROJO";
                }
            }

            contadorPartidas++;

            System.out.println("\n===============================================");
            System.out.println("     PUNTUACIONES INDIVIDUALES ACUMULADAS      ");
            System.out.println("===============================================");
            for (Jugador j : jugadores) {
                System.out.println(" -> " + j.getNombre() + " | Partidas Totales Ganadas: " + j.getJuegosGanados());
            }
            System.out.println("=====================================================");

            if (contadorPartidas < 5) {
                System.out.print("\n¿Desean jugar otra ronda? (S/N): ");
                String respuesta = scanner.nextLine().trim().toUpperCase();
                if (!respuesta.equals("S")) {
                    seguirJugando = false;
                }
            } else {
                System.out.println("\nSe ha completado el espacio máximo para 5 juegos en el arreglo.");
                seguirJugando = false;
            }
        }

        System.out.println("\n======================================");
        System.out.println("       PUNTUACION TOTAL FINAL      ");
        System.out.println("========================================");
        for (int i = 0; i < contadorPartidas; i++) {
            historialPartidas[i].mostrarResumen();
        }
        System.out.println("=============================================");

        System.out.println("\n¡Gracias por jugar! Proceso terminado con éxito.");
        scanner.close();
    }
}