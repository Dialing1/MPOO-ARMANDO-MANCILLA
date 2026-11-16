package mx.unam.fi.smartvend.api.mapper;

import mx.unam.fi.smartvend.api.dto.ProductoResponse;
import mx.unam.fi.smartvend.api.dto.RegistroProductoRequest;
import mx.unam.fi.smartvend.domain.producto.Bebida;
import mx.unam.fi.smartvend.domain.producto.Producto;
import mx.unam.fi.smartvend.domain.producto.ProductoInventario;
import mx.unam.fi.smartvend.domain.producto.Snack;
import org.springframework.stereotype.Component;

/** Convierte entre los DTO de la API y el modelo del dominio. */
@Component
public class ProductoMapper {

    public Producto aProducto(RegistroProductoRequest r) {
        if (r.tipo() != r.categoria())
            throw new IllegalArgumentException("El tipo y la categoria del producto no coinciden");
        return switch (r.tipo()) {
            case BEBIDA -> new Bebida(r.codigo(), r.nombre(), r.fechaCaducidad());
            case SNACK -> new Snack(r.codigo(), r.nombre(), r.fechaCaducidad());
        };
    }

    public ProductoResponse aRespuesta(ProductoInventario pi) {
        Producto p = pi.producto();
        return new ProductoResponse(p.getCodigo(), p.getNombre(), p.getFechaCaducidad(),
                p.getCategoria(), pi.precio(), pi.existencias());
    }
}
