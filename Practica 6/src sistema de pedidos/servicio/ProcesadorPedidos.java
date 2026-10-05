package servicio;

import dominio.*;
import java.math.BigDecimal;
import repositorio.ProductoRepository;

public class ProcesadorPedidos {
    private final ProductoRepository repositorio;

    public ProcesadorPedidos(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void agregarProducto(Pedido pedido, String idProducto, int cantidad) {
        // Se valida todo antes de tocar el pedido
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        Producto producto = repositorio.buscarPorId(idProducto)
            .orElseThrow(() -> new IllegalArgumentException("El producto no existe: " + idProducto));
        pedido.agregar(producto, cantidad);
    }

    public BigDecimal calcularSubtotal(Pedido pedido) {
        BigDecimal subtotal = BigDecimal.ZERO.setScale(2);
        for (ElementoPedido e : pedido.getElementos()) {
            subtotal = subtotal.add(e.getSubtotal());
        }
        return subtotal;
    }

    public BigDecimal calcularDescuento(Pedido pedido) {
        return pedido.getPolitica().calcular(calcularSubtotal(pedido));
    }

    public BigDecimal calcularTotal(Pedido pedido) {
        return calcularSubtotal(pedido).subtract(calcularDescuento(pedido));
    }

    public ResumenPedido confirmar(Pedido pedido) {
        if (pedido.estaVacio()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacío");
        }
        BigDecimal subtotal = calcularSubtotal(pedido);
        BigDecimal descuento = pedido.getPolitica().calcular(subtotal);
        return new ResumenPedido(
            pedido.getId(),
            pedido.getElementos(),
            pedido.getProductosDiferentes(),
            pedido.getTotalUnidades(),
            subtotal,
            descuento,
            subtotal.subtract(descuento));
    }
}
