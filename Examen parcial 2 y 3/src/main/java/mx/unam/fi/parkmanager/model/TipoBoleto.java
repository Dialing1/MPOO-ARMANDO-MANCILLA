package mx.unam.fi.parkmanager.model;

import mx.unam.fi.parkmanager.exception.SolicitudInvalidaException;
import mx.unam.fi.parkmanager.exception.TipoBoletoInvalidoException;

/** Identificadores de las cuatro modalidades (los nombres son parte del contrato REST). */
public enum TipoBoleto {
    GENERAL, PASE_ANUAL, ALUMNO, PROFESOR;

    /** Convierte el texto recibido; solo se aceptan exactamente los cuatro nombres. */
    public static TipoBoleto desde(String texto) {
        if (texto == null) {
            throw new SolicitudInvalidaException("El campo 'tipo' es obligatorio");
        }
        try {
            return valueOf(texto);
        } catch (IllegalArgumentException e) {
            throw new TipoBoletoInvalidoException("Tipo de boleto no valido: " + texto);
        }
    }
}
