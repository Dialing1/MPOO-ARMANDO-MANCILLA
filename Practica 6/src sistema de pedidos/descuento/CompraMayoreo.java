package descuento;

import java.math.BigDecimal;

public class CompraMayoreo implements PoliticaDescuento {
    private static final BigDecimal MINIMO = new BigDecimal("5000.00");

    @Override
    public BigDecimal calcular(BigDecimal subtotal) {
        if (subtotal.compareTo(MINIMO) >= 0) {
            return PoliticaDescuento.porcentaje(subtotal, "0.15");
        }
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public String getNombre() { return "Compra de mayoreo (15%)"; }
}
