package mx.unam.fi.parkmanager.model;

import java.math.BigDecimal;

public class BoletoAlumno extends Boleto {

    private static final BigDecimal PRECIO_BASE = new BigDecimal("650.00");

    @Override
    public TipoBoleto tipo() {
        return TipoBoleto.ALUMNO;
    }

    @Override
    public BigDecimal precioBase() {
        return PRECIO_BASE;
    }
}
