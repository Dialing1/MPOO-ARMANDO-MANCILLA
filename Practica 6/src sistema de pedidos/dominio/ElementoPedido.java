package dominio;

import java.math.BigDecimal;

public final class ElementoPedido {
    private final Producto producto;
    private final int cantidad;

    public ElementoPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }

    public BigDecimal getSubtotal() {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    // Une dos elementos del mismo producto sumando sus cantidades
    public ElementoPedido combinar(ElementoPedido otro) {
        return new ElementoPedido(producto, cantidad + otro.cantidad);
    }

    @Override
    public String toString() {
        return producto.getNombre() + " | $" + producto.getPrecio().toPlainString()
             + " x " + cantidad + " = $" + getSubtotal().toPlainString();
    }
}
