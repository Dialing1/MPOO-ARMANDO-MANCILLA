package dominio;

import descuento.CompraRegular;
import descuento.PoliticaDescuento;
import java.time.LocalDateTime;
import java.util.*;

public class Pedido {
    private final UUID id;
    private final LocalDateTime fechaCreacion;
    private final Map<String, ElementoPedido> elementos = new LinkedHashMap<>();
    private PoliticaDescuento politica;

    private Pedido() {
        this.id = UUID.randomUUID();
        this.fechaCreacion = LocalDateTime.now();
        this.politica = new CompraRegular();
    }

    public static Pedido empty() {
        return new Pedido();
    }

    // Si el producto ya existe, suma las cantidades (un único estado coherente)
    public void agregar(Producto producto, int cantidad) {
        elementos.merge(producto.getId(),
                        new ElementoPedido(producto, cantidad),
                        ElementoPedido::combinar);
    }

    public void cambiarPolitica(PoliticaDescuento politica) {
        this.politica = Objects.requireNonNull(politica);
    }

    public int getTotalUnidades() {
        int total = 0;
        for (ElementoPedido e : elementos.values()) {
            total += e.getCantidad();
        }
        return total;
    }

    public int getProductosDiferentes() { return elementos.size(); }
    public boolean estaVacio() { return elementos.isEmpty(); }

    public UUID getId() { return id; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public PoliticaDescuento getPolitica() { return politica; }

    public List<ElementoPedido> getElementos() {
        return List.copyOf(elementos.values()); // copia: nadie modifica el interior
    }
}
