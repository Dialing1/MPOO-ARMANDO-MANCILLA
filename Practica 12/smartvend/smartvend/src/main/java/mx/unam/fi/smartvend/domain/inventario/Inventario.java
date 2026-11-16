package mx.unam.fi.smartvend.domain.inventario;

import mx.unam.fi.smartvend.domain.producto.Producto;
import mx.unam.fi.smartvend.domain.producto.ProductoInventario;

import java.util.Optional;

public interface Inventario {

    ResultadoRegistro registrarProducto(Producto producto, double precio);

    Optional<ProductoInventario> buscarProducto(String codigo);

    ResultadoReabastecimiento reabastecer(String codigo, int cantidad);

    /** Operacion interna del proceso de compra: NO se expone por HTTP. */
    ResultadoEntrega despachar(String codigo);
}
