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
        // registro de jugadores
System.out.print("¿Cuántos jugadores van a jugar? (mínimo 4): ");
int numJugadores = scanner.nextInt();
scanner.nextLine();

// nombres
String[] nombres = new String[numJugadores];
for (int i = 0; i < numJugadores; i++) {
    System.out.print("Introduce el nombre del jugador " + (i + 1) + ": ");
    nombres[i] = scanner.nextLine();
}

// random de jugadores 
int[] indices = new int[numJugadores];
for (int i = 0; i < numJugadores; i++) indices[i] = i;

for (int i = numJugadores - 1; i > 0; i--) {
    int j = random.nextInt(i + 1);
    int temp = indices[i];
    indices[i] = indices[j];
    indices[j] = temp;
}

//roles aleatorios
Jugador[] jugadores = new Jugador[numJugadores];
for (int i = 0; i < numJugadores; i++) {
    if (i < 2) {
        jugadores[indices[i]] = new EspiaMaestro(nombres[indices[i]]);
    } else {
        jugadores[indices[i]] = new Espia(nombres[indices[i]]);
    }
}

// Asignación de equipos
System.out.println("Asignación de equipos:");
for (int i = 0; i < numJugadores; i++) {
    System.out.print(jugadores[i].getNombre() + " ¿a qué equipo perteneces? (rojo/azul): ");
    String equipo = scanner.nextLine();
    jugadores[i].asignarEquipo(equipo);
    jugadores[i].mostrarInformacion();
}

// Buscar maestros
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
 
           // tablero 
        Tablero tablero = new Tablero(myPalabras);
        maestroRojo.revelarPatronInicio(tablero, scanner);
        maestroAzul.revelarPatronInicio(tablero, scanner);

        //empieza equipo rojo
        String equipoActual = "ROJO";
        boolean juegoTerminado = false;
        int cartasRojas = 9;
        int cartasAzules = 8;
 
        while (!juegoTerminado) {
            System.out.println("\n=--- Turno del equipo " + equipoActual + " ---=");
            System.out.println("cartas rojas restantes: " + cartasRojas + " | cartas azules restantes: " + cartasAzules);
            tablero.mostrarTablero();
 
            // buscar primer espia
           
     for (int i = 0; i < numJugadores; i++) {
    if (jugadores[i] instanceof Espia && !(jugadores[i] instanceof EspiaMaestro) && jugadores[i].getEquipo().equals(equipoActual)) {
        Espia espiaActual = (Espia) jugadores[i];
        espiaActual.adivinarPalabra(tablero, scanner);
        break;
    }
}

} 
 
            // puntos tablero revisando carta por carta
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
 

        
       // verificar si alguien gano 
       if (cartasRojas == 0) {
    System.out.println("Gano el equipo rojo!");
    for (int i = 0; i < numJugadores; i++) {
        if (jugadores[i].getEquipo().equals("ROJO")) {
            jugadores[i].incrementarGanados();
        }
    }
    juegoTerminado = true;
    } else if (cartasAzules == 0) {
    System.out.println("Gano el equipo azul!");
    for (int i = 0; i < numJugadores; i++) {
        if (jugadores[i].getEquipo().equals("AZUL")) {
            jugadores[i].incrementarGanados();
        }
    }
    juegoTerminado = true;
    }  
    for (int f = 0; f < 5; f++) {
    for (int c = 0; c < 5; c++) {
        Carta carta = tablero.getCarta(f, c);
        if (carta.getTipo() == 4 && carta.isRevelada()) {
            System.out.println("El equipo " + equipoActual + " revelo al asesino, perdierooooon!");
            juegoTerminado = true;
        }
        if (!juegoTerminado) {
            equipoActual = equipoActual.equals("ROJO") ? "AZUL" : "ROJO";
        }

        } 

        System.out.println("\n=== FIN DEL JUEGO ===");
        for (int i = 0; i < numJugadores; i++) {
            jugadores[i].mostrarInformacion();
        }
        scanner.close();
  
    } 
  }
}

    
            