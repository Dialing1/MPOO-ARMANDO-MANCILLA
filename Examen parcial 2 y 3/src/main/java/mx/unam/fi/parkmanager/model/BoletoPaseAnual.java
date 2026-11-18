package mx.unam.fi.parkmanager.model;

import java.math.BigDecimal;

public class BoletoPaseAnual extends Boleto {

    private static final BigDecimal PRECIO_BASE = new BigDecimal("2400.00");

    @Override
    public TipoBoleto tipo() {
        return TipoBoleto.PASE_ANUAL;
    }

    @Override
    public BigDecimal precioBase() {
        return PRECIO_BASE;
    }
}
