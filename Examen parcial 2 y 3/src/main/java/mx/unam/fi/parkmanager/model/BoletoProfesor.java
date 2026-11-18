package mx.unam.fi.parkmanager.model;

import java.math.BigDecimal;

public class BoletoProfesor extends Boleto {

    private static final BigDecimal PRECIO_BASE = new BigDecimal("700.00");

    @Override
    public TipoBoleto tipo() {
        return TipoBoleto.PROFESOR;
    }

    @Override
    public BigDecimal precioBase() {
        return PRECIO_BASE;
    }
}
