package dominio;

public final class ReporteEvento {
    private final int totalRegistrados;
    private final int lugaresDisponibles;
    private final int aforoActual;
    private final int alumnos;
    private final int profesores;
    private final int invitados;
    private final double ocupacion;

    public ReporteEvento(int totalRegistrados, int lugaresDisponibles, int aforoActual,
                         int alumnos, int profesores, int invitados, double ocupacion) {
        this.totalRegistrados = totalRegistrados;
        this.lugaresDisponibles = lugaresDisponibles;
        this.aforoActual = aforoActual;
        this.alumnos = alumnos;
        this.profesores = profesores;
        this.invitados = invitados;
        this.ocupacion = ocupacion;
    }

    @Override
    public String toString() {
        return "Total de asistentes registrados: " + totalRegistrados + "\n"
             + "Lugares disponibles: " + lugaresDisponibles + "\n"
             + "Aforo actual: " + aforoActual + "\n"
             + "Total de alumnos dentro el evento: " + alumnos + "\n"
             + "Total de profesores dentro del evento: " + profesores + "\n"
             + "Total de invitados dentro del evento: " + invitados + "\n"
             + String.format("Ocupación: %.0f%%", ocupacion);
    }
}
