package mx.unam.fi.smartvend.domain.pago;

public interface Monedero {

    ResultadoCobro cobrar(double precio, double cantidadRecibida);

    double consultarFondos();
}
