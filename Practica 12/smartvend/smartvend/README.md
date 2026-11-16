# SmartVend API

Maquina expendedora con Spring Boot 4, Maven y Java 25.

## Ejecutar

    mvn spring-boot:run          # levanta la API en http://localhost:8080
    mvn test                     # pruebas
    bash docs/curl.sh            # prueba todos los endpoints (con la API corriendo)

## Estructura (packages de mx.unam.fi.smartvend)

| Package | Responsabilidad |
|---|---|
| domain.producto / inventario / pago / compra | Modelo y reglas fundamentales |
| application.service | Casos de uso (MaquinaExpendedoraService) |
| infrastructure.inventario / pago | Implementaciones concretas (InventarioEnMemoria, MonederoEfectivo, MonederoTarjeta) |
| api.controller / dto / mapper / error | Exposicion HTTP |
| config | Beans (SmartVendConfig) |

Regla de dependencias: `domain` no depende de nadie; `application` solo de `domain`;
`infrastructure` solo de `domain`; `api` de `application` y `domain`; `config` de `infrastructure` y `domain`.

## Endpoints

| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | /api/productos | Registrar producto |
| GET | /api/productos/{codigo} | Buscar producto |
| PATCH | /api/productos/{codigo}/existencias | Reabastecer |
| POST | /api/producto/{codigo}/comprar | Comprar (EFECTIVO o TARJETA) |
| GET | /api/fondos | Fondos por monedero y total |

`Inventario.despachar()` es interno y no tiene endpoint.
