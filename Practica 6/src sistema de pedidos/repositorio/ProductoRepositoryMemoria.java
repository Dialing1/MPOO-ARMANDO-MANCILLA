package repositorio;

import dominio.Producto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductoRepositoryMemoria implements ProductoRepository {
    // Variable estática, según el enunciado
    private static final Map<String, Producto> productos = new HashMap<>();

    @Override
    public boolean guardar(Producto producto) {
        return productos.putIfAbsent(producto.getId(), producto) == null;
    }

    @Override
    public Optional<Producto> buscarPorId(String id) {
        return Optional.ofNullable(productos.get(id));
    }
}
