package descuento;

import java.math.BigDecimal;

public class CompraRegular implements PoliticaDescuento {
    @Override
    public BigDecimal calcular(BigDecimal subtotal) {
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public String getNombre() { return "Compra regular"; }
}
