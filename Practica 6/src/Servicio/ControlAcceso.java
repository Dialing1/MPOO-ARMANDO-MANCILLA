package servicio;

import dominio.*;
import java.util.*;

public class ControlAcceso {
    public static final int AFORO_MAXIMO = 10;

    // Registro de asistentes (variable estática, según el enunciado)
    private static final Map<Integer, Asistente> registrados = new HashMap<>();
    // Estado de los accesos, separado de los datos del asistente
    private static final Set<Integer> dentro = new LinkedHashSet<>();

    private ControlAcceso() { }

    public static boolean registrar(Asistente asistente) {
        if (registrados.containsKey(asistente.getId())) {
            return false; // duplicado: se conserva el estado válido
        }
        registrados.put(asistente.getId(), asistente);
        return true;
    }

    public static ResultadoAcceso entrar(int id) {
        if (!registrados.containsKey(id)) return ResultadoAcceso.NO_REGISTRADO;
        if (dentro.contains(id))          return ResultadoAcceso.YA_DENTRO;
        if (aforoLleno())                 return ResultadoAcceso.AFORO_LLENO;
        dentro.add(id);
        return ResultadoAcceso.AUTORIZADO;
    }

    public static boolean salir(int id) {
        return dentro.remove(id); // false si no estaba dentro, sin modificar nada
    }

    public static List<ResultadoAcceso> entradaMasiva(List<Integer> ids) {
        List<ResultadoAcceso> resultados = new ArrayList<>();
        for (int id : ids) {
            if (aforoLleno()) break;
            ResultadoAcceso r = entrar(id); // reutiliza las reglas de entrar()
            resultados.add(r);
        }
        return resultados;
    }

    public static ReporteEvento generarReporte() {
        Map<TipoAsistente, Integer> conteo = new EnumMap<>(TipoAsistente.class);
        for (TipoAsistente t : TipoAsistente.values()) conteo.put(t, 0);

        for (int id : dentro) {
            TipoAsistente t = registrados.get(id).getTipo();
            conteo.put(t, conteo.get(t) + 1);
        }

        int actual = dentro.size();
        return new ReporteEvento(
            registrados.size(),
            AFORO_MAXIMO - actual,
            actual,
            conteo.get(TipoAsistente.ALUMNO),
            conteo.get(TipoAsistente.PROFESOR),
            conteo.get(TipoAsistente.INVITADO),
            actual * 100.0 / AFORO_MAXIMO);
    }

    private static boolean aforoLleno() {
        return dentro.size() >= AFORO_MAXIMO;
    }
}
