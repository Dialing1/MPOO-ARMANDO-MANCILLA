package mx.unam.fi.parkmanager.exception;

/** Raiz de las excepciones de negocio: cada hija aporta su codigo y su mensaje. */
public abstract class NegocioException extends RuntimeException {

    private final String codigo;

    protected NegocioException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
