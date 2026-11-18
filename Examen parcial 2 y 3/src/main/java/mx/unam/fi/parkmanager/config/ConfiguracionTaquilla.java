package mx.unam.fi.parkmanager.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.unam.fi.parkmanager.model.BoletoAlumno;
import mx.unam.fi.parkmanager.model.BoletoGeneral;
import mx.unam.fi.parkmanager.model.BoletoPaseAnual;
import mx.unam.fi.parkmanager.model.BoletoProfesor;
import mx.unam.fi.parkmanager.model.Taquilla;

@Configuration
public class ConfiguracionTaquilla {

    /** Unica instancia (singleton) con las cuatro modalidades y su estado en memoria. */
    @Bean
    public Taquilla taquilla() {
        return new Taquilla(List.of(new BoletoGeneral(), new BoletoPaseAnual(),
                new BoletoAlumno(), new BoletoProfesor()));
    }
}
