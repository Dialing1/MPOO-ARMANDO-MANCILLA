package dominio;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class ResumenPedido {
    private final UUID id;
    private final List<ElementoPedido> elementos;
    private final int productosDiferentes;
    private final int totalUnidades;
    private final BigDecimal subtotal;
    private final BigDecimal descuento;
    private final BigDecimal total;

    public ResumenPedido(UUID id, List<ElementoPedido> elementos, int productosDiferentes,
                         int totalUnidades, BigDecimal subtotal,
                         BigDecimal descuento, BigDecimal total) {
        this.id = id;
        this.elementos = List.copyOf(elementos);
        this.productosDiferentes = productosDiferentes;
        this.totalUnidades = totalUnidades;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(id).append("\n");
        for (ElementoPedido e : elementos) {
            sb.append("  ").append(e).append("\n");
        }
        sb.append("Productos diferentes: ").append(productosDiferentes).append("\n");
        sb.append("Total de unidades: ").append(totalUnidades).append("\n");
        sb.append("Subtotal: ").append(subtotal.toPlainString()).append("\n");
        sb.append("Descuento: ").append(descuento.toPlainString()).append("\n");
        sb.append("Total: ").append(total.toPlainString());
        return sb.toString();
    }
}
