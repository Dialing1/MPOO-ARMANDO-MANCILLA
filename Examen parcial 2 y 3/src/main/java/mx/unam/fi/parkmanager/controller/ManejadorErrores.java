package mx.unam.fi.parkmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import mx.unam.fi.parkmanager.dto.ErrorResponse;
import mx.unam.fi.parkmanager.exception.NegocioException;

/** Manejo centralizado: toda excepcion de negocio sale como {codigo, mensaje} con HTTP 400. */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> negocio(NegocioException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getCodigo(), e.getMessage()));
    }

    /** JSON mal formado, cuerpo ausente, tipos incorrectos, content-type no soportado. */
    @ExceptionHandler({
            org.springframework.http.converter.HttpMessageNotReadableException.class,
            org.springframework.web.HttpMediaTypeNotSupportedException.class
    })
    public ResponseEntity<ErrorResponse> solicitud(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("SOLICITUD_INVALIDA", "La solicitud es invalida o esta mal formada"));
    }
}
