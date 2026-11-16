package mx.unam.fi.smartvend.api.controller;

import jakarta.validation.Valid;
import mx.unam.fi.smartvend.api.dto.CompraRequest;
import mx.unam.fi.smartvend.api.dto.CompraResponse;
import mx.unam.fi.smartvend.application.service.MaquinaExpendedoraService;
import mx.unam.fi.smartvend.domain.compra.EstadoCompra;
import mx.unam.fi.smartvend.domain.compra.ResultadoCompra;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * La compra es la unica forma de retirar existencias. Inventario.despachar()
 * es una operacion interna y NO tiene endpoint.
 */
@RestController
@RequestMapping("/api")
public class CompraController {

    private final MaquinaExpendedoraService servicio;

    public CompraController(MaquinaExpendedoraService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/producto/{codigo}/comprar")
    public ResponseEntity<CompraResponse> comprar(@PathVariable String codigo,
                                                  @Valid @RequestBody CompraRequest request) {
        if (request.codigoProducto() != null && !request.codigoProducto().equals(codigo))
            throw new IllegalArgumentException("El codigoProducto del cuerpo no coincide con el de la URL");

        ResultadoCompra resultado = servicio.comprar(codigo, request.metodoPago(), request.cantidadRecibida());

        CompraResponse cuerpo = new CompraResponse(
                resultado.estado(),
                codigo,
                resultado.producto() == null ? null : resultado.producto().getNombre(),
                resultado.metodoPago(),
                resultado.cambio());
        return ResponseEntity.status(estadoHttp(resultado.estado())).body(cuerpo);
    }

    private HttpStatus estadoHttp(EstadoCompra estado) {
        return switch (estado) {
            case VENTA_REALIZADA -> HttpStatus.OK;
            case PRODUCTO_NO_ENCONTRADO -> HttpStatus.NOT_FOUND;
            case SIN_EXISTENCIAS, SIN_FONDOS_SUFICIENTES -> HttpStatus.CONFLICT;
            case PAGO_INSUFICIENTE -> HttpStatus.PAYMENT_REQUIRED;
            case PAGO_INVALIDO -> HttpStatus.BAD_REQUEST;
        };
    }
}
