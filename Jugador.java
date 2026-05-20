public abstract class Jugador implements Visualizador {
    protected String nombre;
    protected String equipo;
    protected int juegosGanados;
    protected String rol; // "Espia" o "Espia Maestro"

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.equipo = "SIN EQUIPO";
        this.juegosGanados = 0;
    }

    public void incrementarGanados() {
        this.juegosGanados++;
    }

    public void asignarEquipo(String equipo) {
        this.equipo = equipo.toUpperCase();
    }

    public void asignarEquipo(boolean esRojo) {
        this.equipo = esRojo ? "ROJO" : "AZUL";
    }

    public String getNombre() { return nombre; }
    public String getEquipo() { return equipo; }
    public int getJuegosGanados() { return juegosGanados; }
    public String getRol() { return rol; }

    @Override
    public void mostrarInformacion() {
        System.out.println("- " + nombre + " (" + rol + ") | Equipo: " + equipo + " | Partidas Ganadas: " + juegosGanados);
    }
}