package mx.unam.fi.smartvend.api.controller;

import mx.unam.fi.smartvend.api.dto.FondosResponse;
import mx.unam.fi.smartvend.application.service.MaquinaExpendedoraService;
import mx.unam.fi.smartvend.domain.pago.FondosMaquina;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FondosController {

    private final MaquinaExpendedoraService servicio;

    public FondosController(MaquinaExpendedoraService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/fondos")
    public FondosResponse consultar() {
        FondosMaquina fondos = servicio.consultarFondos();
        return new FondosResponse(fondos.efectivo(), fondos.tarjeta(), fondos.total());
    }
}
