package mx.unam.fi.smartvend.domain.producto;

/** Vista inmutable de un producto junto con su precio y existencias actuales. */
public record ProductoInventario(Producto producto, double precio, int existencias) { }
