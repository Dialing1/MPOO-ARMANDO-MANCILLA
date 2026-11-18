package mx.unam.fi.parkmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mx.unam.fi.parkmanager.dto.CompraRequest;
import mx.unam.fi.parkmanager.dto.CompraResponse;
import mx.unam.fi.parkmanager.exception.CantidadInvalidaException;
import mx.unam.fi.parkmanager.exception.DescuentoInvalidoException;
import mx.unam.fi.parkmanager.exception.SolicitudInvalidaException;
import mx.unam.fi.parkmanager.exception.TipoBoletoInvalidoException;
import mx.unam.fi.parkmanager.model.*;
import mx.unam.fi.parkmanager.service.CompraService;
import mx.unam.fi.parkmanager.service.DescuentoService;

class ParkManagerTest {

    Taquilla taquilla;
    DescuentoService descuentos;
    CompraService compras;

    @BeforeEach
    void preparar() {
        taquilla = new Taquilla(List.of(new BoletoGeneral(), new BoletoPaseAnual(),
                new BoletoAlumno(), new BoletoProfesor()));
        descuentos = new DescuentoService(taquilla);
        compras = new CompraService(taquilla);
    }

    private static BigDecimal d(String s) { return new BigDecimal(s); }

    @Test
    void preciosBase() {
        assertEquals(d("900.00"), new BoletoGeneral().precioBase());
        assertEquals(d("2400.00"), new BoletoPaseAnual().precioBase());
        assertEquals(d("650.00"), new BoletoAlumno().precioBase());
        assertEquals(d("700.00"), new BoletoProfesor().precioBase());
    }

    @Test
    void ejemploDelEnunciado() {
        descuentos.configurar("ALUMNO", d("20"));
        CompraResponse r = compras.comprar(new CompraRequest("ALUMNO", d("3")));
        assertEquals(d("520.00"), r.precioUnitarioFinal());
        assertEquals(d("1560.00"), r.total());
        assertEquals(d("1560.00"), r.ingresosAcumulados());
    }

    @Test
    void descuentosIndependientesYQuitarEsIdempotente() {
        descuentos.configurar("ALUMNO", d("20"));
        assertEquals(d("900.00"), compras.comprar(new CompraRequest("GENERAL", d("1"))).total());
        descuentos.quitar("ALUMNO");
        descuentos.quitar("ALUMNO");
        assertEquals(d("650.00"), compras.comprar(new CompraRequest("ALUMNO", d("1"))).total());
    }

    @Test
    void comprasPasadasNoCambian() {
        descuentos.configurar("PROFESOR", d("50"));
        compras.comprar(new CompraRequest("PROFESOR", d("2")));      // 700
        descuentos.quitar("PROFESOR");
        assertEquals(d("1400.00"), compras.comprar(new CompraRequest("PROFESOR", d("1"))).ingresosAcumulados());
    }

    @Test
    void centavos() {
        descuentos.configurar("GENERAL", d("33.33"));
        assertEquals(d("600.03"), compras.comprar(new CompraRequest("GENERAL", d("1"))).precioUnitarioFinal());
    }

    @Test
    void descuentoLimites() {
        descuentos.configurar("GENERAL", d("0"));
        descuentos.configurar("GENERAL", d("100"));
        assertEquals(d("0.00"), compras.comprar(new CompraRequest("GENERAL", d("1"))).total());
        assertThrows(DescuentoInvalidoException.class, () -> descuentos.configurar("GENERAL", d("-1")));
        assertThrows(DescuentoInvalidoException.class, () -> descuentos.configurar("GENERAL", d("100.01")));
    }

    @Test
    void erroresYAtomicidad() {
        assertThrows(CantidadInvalidaException.class, () -> compras.comprar(new CompraRequest("GENERAL", d("0"))));
        assertThrows(CantidadInvalidaException.class, () -> compras.comprar(new CompraRequest("GENERAL", d("11"))));
        assertThrows(CantidadInvalidaException.class, () -> compras.comprar(new CompraRequest("GENERAL", d("2.5"))));
        assertThrows(TipoBoletoInvalidoException.class, () -> compras.comprar(new CompraRequest("VIP", d("1"))));
        assertThrows(SolicitudInvalidaException.class, () -> compras.comprar(new CompraRequest(null, d("1"))));
        assertThrows(SolicitudInvalidaException.class, () -> compras.comprar(new CompraRequest("GENERAL", null)));
        assertThrows(TipoBoletoInvalidoException.class, () -> descuentos.configurar("VIP", d("10")));
        assertThrows(SolicitudInvalidaException.class, () -> descuentos.configurar("GENERAL", null));
        assertEquals(0, taquilla.getBoletosVendidos());
        assertEquals(d("0.00"), taquilla.getIngresos());
    }
}
