package mx.unam.fi.smartvend.domain.producto;

import java.time.LocalDate;

public class Snack extends Producto {

    public Snack(String codigo, String nombre, LocalDate fechaCaducidad) {
        super(codigo, nombre, fechaCaducidad, CategoriaProducto.SNACK);
    }

    @Override
    public String descripcion() { return "Snack: " + getNombre(); }
}
