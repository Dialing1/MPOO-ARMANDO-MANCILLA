package mx.unam.fi.smartvend.infrastructure.inventario;

import mx.unam.fi.smartvend.domain.inventario.Inventario;
import mx.unam.fi.smartvend.domain.inventario.ResultadoEntrega;
import mx.unam.fi.smartvend.domain.inventario.ResultadoReabastecimiento;
import mx.unam.fi.smartvend.domain.inventario.ResultadoRegistro;
import mx.unam.fi.smartvend.domain.producto.Producto;
import mx.unam.fi.smartvend.domain.producto.ProductoInventario;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Almacenamiento del inventario en memoria (componente de acceso a datos). */
@Repository
public class InventarioEnMemoria implements Inventario {

    private static class Entrada {
        private final Producto producto;
        private final double precio;
        private int existencias;

        private Entrada(Producto producto, double precio) {
            this.producto = producto;
            this.precio = precio;
        }
    }

    private final Map<String, Entrada> productos = new HashMap<>();

    @Override
    public synchronized ResultadoRegistro registrarProducto(Producto producto, double precio) {
        if (producto == null) return ResultadoRegistro.PRODUCTO_INVALIDO;
        if (Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0)
            return ResultadoRegistro.PRECIO_INVALIDO;
        if (productos.containsKey(producto.getCodigo())) return ResultadoRegistro.CODIGO_DUPLICADO;
        productos.put(producto.getCodigo(), new Entrada(producto, precio));
        return ResultadoRegistro.PRODUCTO_REGISTRADO;
    }

    @Override
    public synchronized Optional<ProductoInventario> buscarProducto(String codigo) {
        Entrada e = (codigo == null) ? null : productos.get(codigo);
        if (e == null) return Optional.empty();
        return Optional.of(new ProductoInventario(e.producto, e.precio, e.existencias));
    }

    @Override
    public synchronized ResultadoReabastecimiento reabastecer(String codigo, int cantidad) {
        Entrada e = (codigo == null) ? null : productos.get(codigo);
        if (e == null) return ResultadoReabastecimiento.PRODUCTO_NO_ENCONTRADO;
        if (cantidad <= 0 || (long) e.existencias + cantidad > Integer.MAX_VALUE)
            return ResultadoReabastecimiento.CANTIDAD_INVALIDA;
        e.existencias += cantidad;
        return ResultadoReabastecimiento.REABASTECIMIENTO_REALIZADO;
    }

    @Override
    public synchronized ResultadoEntrega despachar(String codigo) {
        Entrada e = (codigo == null) ? null : productos.get(codigo);
        if (e == null) return ResultadoEntrega.PRODUCTO_NO_ENCONTRADO;
        if (e.existencias <= 0) return ResultadoEntrega.SIN_EXISTENCIAS;
        e.existencias--;
        return ResultadoEntrega.PRODUCTO_PREPARADO;
    }
}
