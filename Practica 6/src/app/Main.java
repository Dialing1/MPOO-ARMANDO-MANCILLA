package app;

import dominio.*;
import servicio.ControlAcceso;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Asistente> asistentes = List.of(
            new Asistente(101, "Ana López", TipoAsistente.ALUMNO),
            new Asistente(102, "Luis Hernández", TipoAsistente.PROFESOR),
            new Asistente(103, "Pedro Ramírez", TipoAsistente.INVITADO),
            new Asistente(104, "Sofía Martínez", TipoAsistente.ALUMNO),
            new Asistente(105, "Carlos Gómez", TipoAsistente.PROFESOR));

        for (Asistente a : asistentes) {
            ControlAcceso.registrar(a);
        }

        List<Integer> intentos = List.of(101, 102, 999, 101, 103, 104);
        List<ResultadoAcceso> resultados = ControlAcceso.entradaMasiva(intentos);

        for (int i = 0; i < resultados.size(); i++) {
            System.out.println(intentos.get(i) + " -> " + resultados.get(i).getMensaje());
        }

        System.out.println();
        System.out.println(ControlAcceso.generarReporte());
    }
}
