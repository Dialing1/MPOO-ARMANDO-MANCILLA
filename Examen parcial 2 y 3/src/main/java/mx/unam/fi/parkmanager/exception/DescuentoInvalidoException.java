package mx.unam.fi.parkmanager.exception;

public class DescuentoInvalidoException extends NegocioException {

    public DescuentoInvalidoException(String mensaje) {
        super("DESCUENTO_INVALIDO", mensaje);
    }
}
