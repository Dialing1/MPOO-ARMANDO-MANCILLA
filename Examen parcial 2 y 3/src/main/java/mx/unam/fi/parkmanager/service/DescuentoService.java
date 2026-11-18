package mx.unam.fi.parkmanager.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import mx.unam.fi.parkmanager.dto.DescuentoResponse;
import mx.unam.fi.parkmanager.exception.SolicitudInvalidaException;
import mx.unam.fi.parkmanager.model.Boleto;
import mx.unam.fi.parkmanager.model.Taquilla;
import mx.unam.fi.parkmanager.model.TipoBoleto;

@Service
public class DescuentoService {

    private final Taquilla taquilla;

    public DescuentoService(Taquilla taquilla) {
        this.taquilla = taquilla;
    }

    public synchronized DescuentoResponse configurar(String tipoTexto, BigDecimal porcentaje) {
        TipoBoleto tipo = TipoBoleto.desde(tipoTexto);
        if (porcentaje == null) {
            throw new SolicitudInvalidaException("El campo 'porcentaje' es obligatorio");
        }
        Boleto modalidad = taquilla.modalidad(tipo);
        modalidad.configurarDescuento(porcentaje);   // valida 0..100 en el modelo
        return new DescuentoResponse(tipo.name(), sinNotacionCientifica(porcentaje), "Descuento configurado");
    }

    public synchronized DescuentoResponse quitar(String tipoTexto) {
        TipoBoleto tipo = TipoBoleto.desde(tipoTexto);
        taquilla.modalidad(tipo).quitarDescuento();
        return new DescuentoResponse(tipo.name(), BigDecimal.ZERO, "Descuento desactivado");
    }

    private static BigDecimal sinNotacionCientifica(BigDecimal valor) {
        return valor.scale() < 0 ? valor.setScale(0) : valor;
    }
}
