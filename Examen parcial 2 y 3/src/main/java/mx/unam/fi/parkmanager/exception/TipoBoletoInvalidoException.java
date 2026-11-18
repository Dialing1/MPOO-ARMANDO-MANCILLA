package mx.unam.fi.parkmanager.exception;

public class TipoBoletoInvalidoException extends NegocioException {

    public TipoBoletoInvalidoException(String mensaje) {
        super("TIPO_BOLETO_INVALIDO", mensaje);
    }
}
