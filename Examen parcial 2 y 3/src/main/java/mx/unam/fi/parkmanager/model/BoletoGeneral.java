package mx.unam.fi.parkmanager.model;

import java.math.BigDecimal;

public class BoletoGeneral extends Boleto {

    private static final BigDecimal PRECIO_BASE = new BigDecimal("900.00");

    @Override
    public TipoBoleto tipo() {
        return TipoBoleto.GENERAL;
    }

    @Override
    public BigDecimal precioBase() {
        return PRECIO_BASE;
    }
}
