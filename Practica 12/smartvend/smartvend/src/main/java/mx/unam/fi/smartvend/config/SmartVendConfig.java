package mx.unam.fi.smartvend.config;

import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.infrastructure.pago.MonederoEfectivo;
import mx.unam.fi.smartvend.infrastructure.pago.MonederoTarjeta;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Construye los dos monederos. Como ambos son de tipo Monedero, cada bean tiene
 * un nombre y un @Qualifier para que Spring sepa cual inyectar en cada lugar.
 */
@Configuration
public class SmartVendConfig {

    @Bean
    @Qualifier("monederoEfectivo")
    public Monedero monederoEfectivo(@Value("${smartvend.fondos.efectivo}") double fondosIniciales) {
        return new MonederoEfectivo(fondosIniciales);
    }

    @Bean
    @Qualifier("monederoTarjeta")
    public Monedero monederoTarjeta(@Value("${smartvend.fondos.tarjeta}") double fondosIniciales) {
        return new MonederoTarjeta(fondosIniciales);
    }
}
