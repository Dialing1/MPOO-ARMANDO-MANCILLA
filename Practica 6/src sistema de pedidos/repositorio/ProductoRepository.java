package repositorio;

import dominio.Producto;
import java.util.Optional;

public interface ProductoRepository {
    boolean guardar(Producto producto);
    Optional<Producto> buscarPorId(String id);
}
