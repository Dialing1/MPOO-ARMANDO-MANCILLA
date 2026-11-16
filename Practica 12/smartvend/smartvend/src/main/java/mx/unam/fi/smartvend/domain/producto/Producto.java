package mx.unam.fi.smartvend.domain.producto;

import java.time.LocalDate;

/**
 * Producto de la maquina. No administra precio ni existencias: eso es
 * responsabilidad del Inventario.
 */
public abstract class Producto {

    private final String codigo;
    private final String nombre;
    private final LocalDate fechaCaducidad;
    private final CategoriaProducto categoria;

    protected Producto(String codigo, String nombre, LocalDate fechaCaducidad, CategoriaProducto categoria) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("El codigo del producto es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        if (fechaCaducidad == null)
            throw new IllegalArgumentException("La fecha de caducidad es obligatoria");
        if (categoria == null)
            throw new IllegalArgumentException("La categoria es obligatoria");
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.fechaCaducidad = fechaCaducidad;
        this.categoria = categoria;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public CategoriaProducto getCategoria() { return categoria; }

    public abstract String descripcion();
}
