package mx.unam.fi.parkmanager.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.unam.fi.parkmanager.dto.DescuentoRequest;
import mx.unam.fi.parkmanager.dto.DescuentoResponse;
import mx.unam.fi.parkmanager.service.DescuentoService;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    private final DescuentoService servicio;

    public DescuentoController(DescuentoService servicio) {
        this.servicio = servicio;
    }

    @PutMapping("/{tipo}")
    public DescuentoResponse configurar(@PathVariable String tipo, @RequestBody DescuentoRequest cuerpo) {
        return servicio.configurar(tipo, cuerpo.porcentaje());
    }

    @DeleteMapping("/{tipo}")
    public DescuentoResponse quitar(@PathVariable String tipo) {
        return servicio.quitar(tipo);
    }
}
