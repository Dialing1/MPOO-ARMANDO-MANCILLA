package mx.unam.fi.smartvend.api.controller;

import jakarta.validation.Valid;
import mx.unam.fi.smartvend.api.dto.ErrorResponse;
import mx.unam.fi.smartvend.api.dto.ProductoResponse;
import mx.unam.fi.smartvend.api.dto.ReabastecimientoRequest;
import mx.unam.fi.smartvend.api.dto.RegistroProductoRequest;
import mx.unam.fi.smartvend.api.mapper.ProductoMapper;
import mx.unam.fi.smartvend.application.service.MaquinaExpendedoraService;
import mx.unam.fi.smartvend.domain.inventario.ResultadoReabastecimiento;
import mx.unam.fi.smartvend.domain.inventario.ResultadoRegistro;
import mx.unam.fi.smartvend.domain.producto.Producto;
import mx.unam.fi.smartvend.domain.producto.ProductoInventario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

/** Solo traduce HTTP <-> casos de uso. No contiene logica de negocio. */
@RestController
@RequestMapping("/api")
public class ProductoController {

    private final MaquinaExpendedoraService servicio;
    private final ProductoMapper mapper;

    public ProductoController(MaquinaExpendedoraService servicio, ProductoMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    @PostMapping("/productos")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroProductoRequest request) {
        Producto producto = mapper.aProducto(request);
        ResultadoRegistro resultado = servicio.registrarProducto(producto, request.precio());

        return switch (resultado) {
            case PRODUCTO_REGISTRADO -> {
                ProductoResponse cuerpo = mapper.aRespuesta(servicio.buscarProducto(producto.getCodigo()).orElseThrow());
                yield ResponseEntity.created(URI.create("/api/productos/" + producto.getCodigo())).body(cuerpo);
            }
            case CODIGO_DUPLICADO -> ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Ya existe un producto con el codigo " + producto.getCodigo()));
            case PRODUCTO_INVALIDO, PRECIO_INVALIDO -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Producto o precio invalido: " + resultado));
        };
    }

    @GetMapping("/productos/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable String codigo) {
        Optional<ProductoInventario> encontrado = servicio.buscarProducto(codigo);
        if (encontrado.isEmpty()) return noEncontrado(codigo);
        return ResponseEntity.ok(mapper.aRespuesta(encontrado.get()));
    }

    @PatchMapping("/productos/{codigo}/existencias")
    public ResponseEntity<?> reabastecer(@PathVariable String codigo,
                                         @Valid @RequestBody ReabastecimientoRequest request) {
        ResultadoReabastecimiento resultado = servicio.reabastecer(codigo, request.cantidad());

        return switch (resultado) {
            case REABASTECIMIENTO_REALIZADO ->
                    ResponseEntity.ok(mapper.aRespuesta(servicio.buscarProducto(codigo).orElseThrow()));
            case PRODUCTO_NO_ENCONTRADO -> noEncontrado(codigo);
            case CANTIDAD_INVALIDA -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("La cantidad para reabastecer es invalida"));
        };
    }

    private ResponseEntity<ErrorResponse> noEncontrado(String codigo) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("No existe el producto " + codigo));
    }
}
