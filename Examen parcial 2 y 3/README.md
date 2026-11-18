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

Tambien se puede ejecutar `evidencia.ps1` con el servidor encendido; recorre todos los casos (exitosos y de error) y guarda las respuestas en `evidencia.txt`.

## Justificacion del diseno

### Herencia: `Boleto` abstracta y una hija por modalidad
Las cuatro modalidades comparten exactamente el mismo comportamiento (guardar un descuento, validarlo, quitarlo y calcular el precio final) y solo difieren en su precio base y su identificador. Por eso ese comportamiento comun vive una sola vez en la clase abstracta `Boleto`, y cada hija (`BoletoGeneral`, `BoletoPaseAnual`, `BoletoAlumno`, `BoletoProfesor`) solo implementa lo que la distingue. Asi no se repite codigo y agregar una modalidad nueva consiste en crear una clase hija y registrarla en `ConfiguracionTaquilla`, sin tocar el servicio ni el controlador.

### Polimorfismo en lugar de if-else / switch / instanceof
El servicio de compras obtiene la modalidad del catalogo y simplemente llama `modalidad.precioBase()` y `modalidad.precioFinal()`. Java decide en tiempo de ejecucion que implementacion usar, de modo que el servicio nunca pregunta "que tipo de boleto es". Esto cumple la restriccion del enunciado y mantiene las decisiones de cada modalidad dentro del modelo.

### Encapsulamiento y constructores
- El descuento es un atributo `private` de `Boleto`; solo cambia mediante `configurarDescuento` (que valida 0 a 100) y `quitarDescuento`. Es imposible dejar un boleto con un descuento invalido.
- El precio base es una constante `private static final` en cada hija: las tarifas no pueden modificarse, como pide el enunciado.
- `Taquilla` recibe sus modalidades por constructor y expone los acumulados solo con getters; los contadores solo cambian en `registrarVenta`.
- Los servicios y controladores reciben sus dependencias por constructor (inyeccion de Spring), con campos `final`.

### Separacion en paquetes
Cada paquete tiene una responsabilidad: `model` contiene las reglas del dominio, `service` coordina validaciones y casos de uso, `controller` solo traduce HTTP/JSON a llamadas al servicio (sin formulas ni contadores), `dto` define el contrato JSON, `exception` los errores de negocio y `config` el armado de objetos. Asi un cambio en la API no afecta al modelo y viceversa.

### Excepciones propias y manejo centralizado
`NegocioException` es la raiz abstracta y guarda el `codigo`; cada hija (`DescuentoInvalidoException`, `CantidadInvalidaException`, `TipoBoletoInvalidoException`, `SolicitudInvalidaException`) fija su codigo. Las excepciones se lanzan donde se detecta el error (modelo o servicio) y `ManejadorErrores` (`@RestControllerAdvice`) las convierte en un unico lugar a HTTP 400 con `{codigo, mensaje}`. El JSON mal formado tambien se traduce a `SOLICITUD_INVALIDA`. Los controladores no tienen `try/catch`.

### Otras decisiones
- **`BigDecimal` para dinero:** evita los errores de redondeo de `double`; el precio unitario se redondea a centavos (`HALF_UP`).
- **Compra atomica:** `CompraService.comprar` valida tipo y cantidad y calcula todo antes de llamar a `registrarVenta`, que es el unico punto que modifica estado. Si algo falla, los contadores quedan intactos. Los metodos de servicio son `synchronized` para que dos solicitudes simultaneas no se mezclen.
- **Compras pasadas inmutables:** cada compra se calcula con el descuento vigente en ese momento y su total se suma a los ingresos; cambiar o quitar el descuento despues solo afecta compras futuras.
- **Cantidad como `BigDecimal` en el DTO:** permite rechazar valores como `2.5` con `CANTIDAD_INVALIDA` en vez de truncarlos silenciosamente.
- **`enum TipoBoleto`:** fija los cuatro identificadores exactos y sirve de llave del `EnumMap` del catalogo; un texto distinto produce `TIPO_BOLETO_INVALIDO`.
- **Un solo bean `Taquilla`:** el estado vive en memoria en una unica instancia compartida, ya que el enunciado no requiere persistencia.
