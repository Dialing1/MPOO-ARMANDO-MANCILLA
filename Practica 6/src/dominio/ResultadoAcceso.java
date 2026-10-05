package dominio;

public enum ResultadoAcceso {
    AUTORIZADO("acceso autorizado"),
    NO_REGISTRADO("persona no registrada"),
    YA_DENTRO("acceso rechazado, ya se encuentra dentro"),
    AFORO_LLENO("acceso rechazado, aforo máximo alcanzado");

    private final String mensaje;

    ResultadoAcceso(String mensaje) { this.mensaje = mensaje; }

    public String getMensaje() { return mensaje; }
}
