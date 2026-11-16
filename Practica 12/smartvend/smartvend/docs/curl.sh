#!/usr/bin/env bash
# Prueba de cada endpoint de SmartVend (la aplicacion debe estar corriendo en el puerto 8080)
BASE=http://localhost:8080/api
H='Content-Type: application/json'

echo "== 1. Registrar producto (201)"
curl -i -X POST $BASE/productos -H "$H" -d '{"tipo":"BEBIDA","codigo":"B001","nombre":"Agua","fechaCaducidad":"2027-05-20","categoria":"BEBIDA","precio":18.50}'

echo; echo "== 2. Registrar el mismo codigo (409)"
curl -i -X POST $BASE/productos -H "$H" -d '{"tipo":"BEBIDA","codigo":"B001","nombre":"Agua","fechaCaducidad":"2027-05-20","categoria":"BEBIDA","precio":18.50}'

echo; echo "== 3. Buscar producto (200, existencias 0)"
curl -i $BASE/productos/B001

echo; echo "== 4. Buscar producto inexistente (404)"
curl -i $BASE/productos/NOEXISTE

echo; echo "== 5. Reabastecer 10 unidades (200)"
curl -i -X PATCH $BASE/productos/B001/existencias -H "$H" -d '{"cantidad":10}'

echo; echo "== 6. Reabastecer con cantidad invalida (400)"
curl -i -X PATCH $BASE/productos/B001/existencias -H "$H" -d '{"cantidad":0}'

echo; echo "== 7. Comprar con efectivo (200, cambio 1.5)"
curl -i -X POST $BASE/producto/B001/comprar -H "$H" -d '{"codigoProducto":"B001","metodoPago":"EFECTIVO","cantidadRecibida":20.00}'

echo; echo "== 8. Comprar con tarjeta (200, cambio 0)"
curl -i -X POST $BASE/producto/B001/comprar -H "$H" -d '{"codigoProducto":"B001","metodoPago":"TARJETA","cantidadRecibida":18.50}'

echo; echo "== 9. Pago insuficiente (402)"
curl -i -X POST $BASE/producto/B001/comprar -H "$H" -d '{"codigoProducto":"B001","metodoPago":"EFECTIVO","cantidadRecibida":5.00}'

echo; echo "== 10. Tarjeta con monto distinto al precio (400)"
curl -i -X POST $BASE/producto/B001/comprar -H "$H" -d '{"codigoProducto":"B001","metodoPago":"TARJETA","cantidadRecibida":30.00}'

echo; echo "== 11. Consultar fondos (200)"
curl -i $BASE/fondos

echo; echo "== 12. despachar NO esta expuesto (404 o 405)"
curl -i -X POST $BASE/despachar
