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

        // --- Registro de jugadores ---
        System.out.print("¿Cuántos jugadores van a jugar? (mínimo 4): ");
        int numJugadores = scanner.nextInt();
        scanner.nextLine(); // FIX: limpiar buffer después de nextInt

        String[] nombres = new String[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            System.out.print("Introduce el nombre del jugador " + (i + 1) + ": ");
            nombres[i] = scanner.nextLine();
        }

        // Mezclar índices para asignar roles al azar
        int[] indices = new int[numJugadores];
        for (int i = 0; i < numJugadores; i++) indices[i] = i;
        for (int i = numJugadores - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = indices[i];
            indices[i] = indices[j];
            indices[j] = temp;
        }

        // Asignar roles: los dos primeros mezclados son EspiaMaestro
        Jugador[] jugadores = new Jugador[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            if (i < 2) {
                jugadores[indices[i]] = new EspiaMaestro(nombres[indices[i]]);
            } else {
                jugadores[indices[i]] = new Espia(nombres[indices[i]]);
            }
        }

        // --- Asignación de equipos ---
        // FIX: el primer maestro va a ROJO, el segundo a AZUL automáticamente
        // para garantizar que haya un maestro por equipo
        boolean primerMaestro = true;
        for (int i = 0; i < numJugadores; i++) {
            if (jugadores[indices[i]] instanceof EspiaMaestro) {
                jugadores[indices[i]].asignarEquipo(primerMaestro ? "ROJO" : "AZUL");
                System.out.println(jugadores[indices[i]].getNombre() + " → Espía Maestro equipo " + jugadores[indices[i]].getEquipo());
                primerMaestro = false;
            }
        }

        // Los espías normales eligen su equipo
        System.out.println("\nAsignación de equipos para los espías:");
        for (int i = 0; i < numJugadores; i++) {
            if (!(jugadores[i] instanceof EspiaMaestro)) {
                System.out.print(jugadores[i].getNombre() + " ¿a qué equipo perteneces? (rojo/azul): ");
                String equipo = scanner.nextLine();
                jugadores[i].asignarEquipo(equipo);
            }
            jugadores[i].mostrarInformacion();
        }

        // Buscar maestros por equipo
        EspiaMaestro maestroRojo = null;
        EspiaMaestro maestroAzul = null;
        for (int i = 0; i < numJugadores; i++) {
            if (jugadores[i] instanceof EspiaMaestro) {
                if (jugadores[i].getEquipo().equals("ROJO")) {
                    maestroRojo = (EspiaMaestro) jugadores[i];
                } else {
                    maestroAzul = (EspiaMaestro) jugadores[i];
                }
            }
        }

        // --- Tablero y fase secreta ---
        Tablero tablero = new Tablero(myPalabras);
        maestroRojo.revelarPatronInicio(tablero, scanner);
        maestroAzul.revelarPatronInicio(tablero, scanner);

        // --- Bucle principal del juego ---
        String equipoActual = "ROJO";
        boolean juegoTerminado = false;
        int cartasRojas = 9;
        int cartasAzules = 8;

        // FIX: índice rotatorio por equipo para que turnen entre espías del mismo equipo
        int turnoRojo = 0;
        int turnoAzul = 0;

        while (!juegoTerminado) {
            System.out.println("\n=== Turno del equipo " + equipoActual + " ===");
            System.out.println("Cartas rojas restantes: " + cartasRojas + " | Cartas azules restantes: " + cartasAzules);
            tablero.mostrarTablero();

            // FIX: recopilar espías del equipo actual y rotar entre ellos
            Espia[] espias = new Espia[numJugadores];
            int numEspias = 0;
            for (int i = 0; i < numJugadores; i++) {
                if (jugadores[i] instanceof Espia
                        && !(jugadores[i] instanceof EspiaMaestro)
                        && jugadores[i].getEquipo().equals(equipoActual)) {
                    espias[numEspias++] = (Espia) jugadores[i];
                }
            }

            if (numEspias > 0) {
                // Rotar turno según el equipo
                int turnoActual;
                if (equipoActual.equals("ROJO")) {
                    turnoActual = turnoRojo % numEspias;
                    turnoRojo++;
                } else {
                    turnoActual = turnoAzul % numEspias;
                    turnoAzul++;
                }
                espias[turnoActual].adivinarPalabra(tablero, scanner);
            } else {
                System.out.println("No hay espías disponibles para el equipo " + equipoActual);
            }

            // --- Recuento de cartas restantes ---
            cartasRojas = 0;
            cartasAzules = 0;
            for (int f = 0; f < 5; f++) {
                for (int c = 0; c < 5; c++) {
                    Carta carta = tablero.getCarta(f, c);
                    if (!carta.isRevelada()) {
                        if (carta.getTipo() == 1) cartasRojas++;
                        if (carta.getTipo() == 2) cartasAzules++;
                    }
                }
            }

            // --- Verificar asesino revelado ---
            for (int f = 0; f < 5 && !juegoTerminado; f++) {
                for (int c = 0; c < 5 && !juegoTerminado; c++) {
                    Carta carta = tablero.getCarta(f, c);
                    if (carta.getTipo() == 4 && carta.isRevelada()) {
                        System.out.println("¡El equipo " + equipoActual + " reveló al asesino! ¡Pierden!");
                        juegoTerminado = true;
                    }
                }
            }

            // --- Verificar victoria por cartas ---
            if (!juegoTerminado) {
                if (cartasRojas == 0) {
                    System.out.println("¡Ganó el equipo ROJO!");
                    for (int i = 0; i < numJugadores; i++) {
                        if (jugadores[i].getEquipo().equals("ROJO")) jugadores[i].incrementarGanados();
                    }
                    juegoTerminado = true;
                } else if (cartasAzules == 0) {
                    System.out.println("¡Ganó el equipo AZUL!");
                    for (int i = 0; i < numJugadores; i++) {
                        if (jugadores[i].getEquipo().equals("AZUL")) jugadores[i].incrementarGanados();
                    }
                    juegoTerminado = true;
                }
            }

            // --- Cambiar turno ---
            if (!juegoTerminado) {
                equipoActual = equipoActual.equals("ROJO") ? "AZUL" : "ROJO";
            }
        }

        // --- Fin del juego ---
        System.out.println("\n=== FIN DEL JUEGO ===");
        for (int i = 0; i < numJugadores; i++) {
            jugadores[i].mostrarInformacion();
        }
        scanner.close();
    }
}