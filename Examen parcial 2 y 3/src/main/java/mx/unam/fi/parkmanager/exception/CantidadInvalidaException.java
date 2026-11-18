package mx.unam.fi.parkmanager.exception;

public class CantidadInvalidaException extends NegocioException {

    public CantidadInvalidaException(String mensaje) {
        super("CANTIDAD_INVALIDA", mensaje);
    }
}
