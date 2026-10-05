package app;

import descuento.*;
import dominio.*;
import java.math.BigDecimal;
import java.util.List;
import repositorio.*;
import servicio.ProcesadorPedidos;

public class Main {
    public static void main(String[] args) {
        ProductoRepository repo = new ProductoRepositoryMemoria();
        repo.guardar(new Producto("P1", "Teclado", new BigDecimal("850.00")));
        repo.guardar(new Producto("P2", "Mouse", new BigDecimal("450.00")));
        repo.guardar(new Producto("P3", "Monitor", new BigDecimal("3500.00")));

        ProcesadorPedidos procesador = new ProcesadorPedidos(repo);

        List<PoliticaDescuento> politicas =
            List.of(new CompraRegular(), new ClienteFrecuente(), new CompraMayoreo());

        for (PoliticaDescuento politica : politicas) {
            Pedido pedido = armarPedido(procesador);
            pedido.cambiarPolitica(politica);
            System.out.println("=== " + politica.getNombre() + " ===");
            System.out.println(procesador.confirmar(pedido));
            System.out.println();
        }

        // Operaciones inválidas: el pedido no cambia
        Pedido pedido = Pedido.empty();
        try {
            procesador.agregarProducto(pedido, "P1", 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }
        try {
            procesador.agregarProducto(pedido, "XX", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }
    }

    private static Pedido armarPedido(ProcesadorPedidos procesador) {
        Pedido pedido = Pedido.empty();
        procesador.agregarProducto(pedido, "P1", 2);
        procesador.agregarProducto(pedido, "P2", 1);
        procesador.agregarProducto(pedido, "P2", 2); // mismo producto: se suma (total 3)
        procesador.agregarProducto(pedido, "P3", 1);
        return pedido;
    }
}
