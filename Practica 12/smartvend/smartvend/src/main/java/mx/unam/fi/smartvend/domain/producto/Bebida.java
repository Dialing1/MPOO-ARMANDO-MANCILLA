package mx.unam.fi.smartvend.domain.producto;

import java.time.LocalDate;

public class Bebida extends Producto {

    public Bebida(String codigo, String nombre, LocalDate fechaCaducidad) {
        super(codigo, nombre, fechaCaducidad, CategoriaProducto.BEBIDA);
    }

    @Override
    public String descripcion() { return "Bebida: " + getNombre(); }
}
