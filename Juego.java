class Juego {
    private int idPartida;
    private String ganador;

    public Juego(int idPartida) {
        this.idPartida = idPartida;
        this.ganador = "NO COMPLETADO";
    }

    public void registrarGanador(String equipo) {
        this.ganador = equipo;
    }

    public void mostrarResumen() {
        System.out.println(" Partida Num: " + idPartida + " -> Ganador del encuentro: Equipo " + ganador);
    }
}

