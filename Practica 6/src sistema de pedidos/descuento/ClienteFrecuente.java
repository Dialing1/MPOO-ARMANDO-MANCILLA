package descuento;

import java.math.BigDecimal;

public class ClienteFrecuente implements PoliticaDescuento {
    @Override
    public BigDecimal calcular(BigDecimal subtotal) {
        return PoliticaDescuento.porcentaje(subtotal, "0.10");
    }

    @Override
    public String getNombre() { return "Cliente frecuente (10%)"; }
}
