package mx.unam.fi.parkmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.unam.fi.parkmanager.dto.CompraRequest;
import mx.unam.fi.parkmanager.dto.CompraResponse;
import mx.unam.fi.parkmanager.service.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService servicio;

    public CompraController(CompraService servicio) {
        this.servicio = servicio;
    }

    /** El enunciado pide HTTP 200 en una compra exitosa. */
    @PostMapping
    public ResponseEntity<CompraResponse> comprar(@RequestBody CompraRequest cuerpo) {
        return ResponseEntity.ok(servicio.comprar(cuerpo));
    }
}
