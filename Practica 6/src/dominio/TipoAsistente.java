package dominio;

import java.util.Objects;

public final class Asistente {
    private final int id;
    private final String nombre;
    private final TipoAsistente tipo;

    public Asistente(int id, String nombre, TipoAsistente tipo) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre);
        this.tipo = Objects.requireNonNull(tipo);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoAsistente getTipo() { return tipo; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Asistente a && a.id == id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public String toString() { return id + " " + nombre + " (" + tipo + ")"; }
}
