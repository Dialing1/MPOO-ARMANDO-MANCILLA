# ParkManager - Venta de boletos Six Flags

Evaluacion practica Unidades 2 y 3 (herencia, polimorfismo y excepciones). Java 21+, Spring Boot, Maven.

## Ejecutar
```
mvn spring-boot:run        # servidor en http://localhost:8080
mvn test                   # pruebas
```

## Estructura de paquetes (mx.unam.fi.parkmanager)
| Paquete | Contenido |
|---|---|
| `model` | `Boleto` (abstracta) y sus hijas `BoletoGeneral`, `BoletoPaseAnual`, `BoletoAlumno`, `BoletoProfesor`; `TipoBoleto`; `Taquilla` (estado en memoria) |
| `service` | `DescuentoService`, `CompraService` (reglas y validaciones, compra atomica) |
| `controller` | `DescuentoController`, `CompraController`, `ManejadorErrores` (`@RestControllerAdvice`) |
| `dto` | Records de entrada y salida JSON |
| `exception` | `NegocioException` y 4 hijas con su codigo |
| `config` | Bean unico de `Taquilla` con las 4 modalidades |

## Endpoints
| Metodo | Ruta | Cuerpo | Respuesta |
|---|---|---|---|
| PUT | `/api/descuentos/{tipo}` | `{"porcentaje":20}` | 200 `{"tipo":"ALUMNO","porcentaje":20,"mensaje":"Descuento configurado"}` |
| DELETE | `/api/descuentos/{tipo}` | - | 200 (idempotente) |
| POST | `/api/compras` | `{"tipo":"ALUMNO","cantidad":3}` | 200 con precioBase, descuentoAplicado, precioUnitarioFinal, total, ingresosAcumulados |

Errores (HTTP 400, cuerpo `{"codigo","mensaje"}`): `DESCUENTO_INVALIDO`, `CANTIDAD_INVALIDA`, `TIPO_BOLETO_INVALIDO`, `SOLICITUD_INVALIDA`.

## Prueba rapida
```
curl -X PUT  localhost:8080/api/descuentos/ALUMNO -H "Content-Type: application/json" -d '{"porcentaje":20}'
curl -X POST localhost:8080/api/compras -H "Content-Type: application/json" -d '{"tipo":"ALUMNO","cantidad":3}'
curl -X DELETE localhost:8080/api/descuentos/ALUMNO
curl -X POST localhost:8080/api/compras -H "Content-Type: application/json" -d '{"tipo":"ALUMNO","cantidad":11}'
```
