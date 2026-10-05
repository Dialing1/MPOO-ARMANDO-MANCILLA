package descuento;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface PoliticaDescuento {
    /** Devuelve el importe a descontar (no el total). */
    BigDecimal calcular(BigDecimal subtotal);

    String getNombre();

    // Cálculo de porcentaje compartido, para no repetirlo en cada política
    static BigDecimal porcentaje(BigDecimal base, String factor) {
        return base.multiply(new BigDecimal(factor)).setScale(2, RoundingMode.HALF_UP);
    }
}
