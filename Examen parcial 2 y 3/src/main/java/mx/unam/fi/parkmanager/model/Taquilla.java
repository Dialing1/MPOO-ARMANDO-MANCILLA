package mx.unam.fi.parkmanager.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Catalogo de modalidades y acumulados de venta. Es quien guarda el estado en memoria.
 * Los metodos que cambian estado reciben ya los valores validados (el servicio valida antes).
 */
public class Taquilla {

    private final Map<TipoBoleto, Boleto> catalogo = new EnumMap<>(TipoBoleto.class);
    private int boletosVendidos = 0;
    private BigDecimal ingresos = BigDecimal.ZERO.setScale(2);

    public Taquilla(List<Boleto> modalidades) {
        for (Boleto b : modalidades) {
            catalogo.put(b.tipo(), b);
        }
    }

    /** Devuelve la modalidad del tipo pedido (el catalogo se llena con las cuatro). */
    public Boleto modalidad(TipoBoleto tipo) {
        return catalogo.get(tipo);
    }

    public void registrarVenta(int cantidad, BigDecimal total) {
        boletosVendidos += cantidad;
        ingresos = ingresos.add(total);
    }

    public int getBoletosVendidos() {
        return boletosVendidos;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }
}
