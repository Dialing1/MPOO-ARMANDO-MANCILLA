package mx.unam.fi.parkmanager.exception;

public class SolicitudInvalidaException extends NegocioException {

    public SolicitudInvalidaException(String mensaje) {
        super("SOLICITUD_INVALIDA", mensaje);
    }
}
