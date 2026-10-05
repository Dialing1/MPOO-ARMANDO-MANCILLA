package dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Producto {
    private final String id;
    private final String nombre;
    private final BigDecimal precio;

    public Producto(String id, String nombre, BigDecimal precio) {
        this.id = Objects.requireNonNull(id);
        this.nombre = Objects.requireNonNull(nombre);
        Objects.requireNonNull(precio);
        if (precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        this.precio = precio.setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
}
