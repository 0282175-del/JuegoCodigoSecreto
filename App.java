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

       
        Jugador[] jugadores = new Jugador[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            jugadores[i] = new Espia(nombres[i]);
        }

        
        System.out.println("\nAsignación de equipos:");
        for (int i = 0; i < numJugadores; i++) {
            System.out.print(jugadores[i].getNombre() + " ¿a qué equipo perteneces? (rojo/azul): ");
            String equipo = scanner.nextLine();
            jugadores[i].asignarEquipo(equipo);
        }

      
        
        int[] indicesRojo = new int[numJugadores];
        int[] indicesAzul = new int[numJugadores];
        int cntRojo = 0, cntAzul = 0;
        for (int i = 0; i < numJugadores; i++) {
            if (jugadores[i].getEquipo().equals("ROJO")) indicesRojo[cntRojo++] = i;
            else indicesAzul[cntAzul++] = i;
        }

        // Elegir maestro aleatorio de cada equipo y convertirlo
        int iMaestroRojo = indicesRojo[random.nextInt(cntRojo)];
        int iMaestroAzul = indicesAzul[random.nextInt(cntAzul)];

        jugadores[iMaestroRojo] = new EspiaMaestro(nombres[iMaestroRojo]);
        jugadores[iMaestroRojo].asignarEquipo("ROJO");

        jugadores[iMaestroAzul] = new EspiaMaestro(nombres[iMaestroAzul]);
        jugadores[iMaestroAzul].asignarEquipo("AZUL");

        System.out.println("\n=== Roles asignados ===");
        for (int i = 0; i < numJugadores; i++) {
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

       //tablero 
        Tablero tablero = new Tablero(myPalabras);
        maestroRojo.revelarPatronInicio(tablero, scanner);
        maestroAzul.revelarPatronInicio(tablero, scanner);

        // --- Fase de juego ---
        String equipoActual = "ROJO";
        boolean juegoTerminado = false;
        int cartasRojas = 9;
        int cartasAzules = 8;

        
        int turnoRojo = 0;
        int turnoAzul = 0;

        while (!juegoTerminado) {
            System.out.println("\n=== Turno del equipo " + equipoActual + " ===");
            System.out.println("Cartas rojas restantes: " + cartasRojas + " | Cartas azules restantes: " + cartasAzules);
            tablero.mostrarTablero();

            // recopilar espías del equipo actual y rotar entre ellos
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

            // recalcular cartas restantes después de cada turno
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

            // verificar si el equipo actual reveló al asesino
            for (int f = 0; f < 5 && !juegoTerminado; f++) {
                for (int c = 0; c < 5 && !juegoTerminado; c++) {
                    Carta carta = tablero.getCarta(f, c);
                    if (carta.getTipo() == 4 && carta.isRevelada()) {
                        System.out.println("¡El equipo " + equipoActual + " reveló al asesino! ¡Pierden!");
                        juegoTerminado = true;
                    }
                }
            }

           // verificar si algún equipo ganó al revelar todas sus cartas
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

            
            if (!juegoTerminado) {
                equipoActual = equipoActual.equals("ROJO") ? "AZUL" : "ROJO";
            }
        }

        
        System.out.println("--fin del juego---");
        for (int i = 0; i < numJugadores; i++) {
            jugadores[i].mostrarInformacion();
        }
        scanner.close();
    }
}