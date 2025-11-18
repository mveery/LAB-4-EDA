package modelos;

import estructura.ColaPrioridadPaciente;
import estructura.HashPacientesEncadenado;
import estructura.ArbolPacientes;
import java.util.Map;
import java.util.List;

public class Hospital {
    private Map<String, Paciente> salaEspera;
    private ColaPrioridadPaciente colaAtencion;
    private ArbolPacientes historicoPacientes;

    public Hospital(int capacidadSala) {
        this.salaEspera = new HashPacientesEncadenado(capacidadSala);

        this.colaAtencion = new ColaPrioridadPaciente();
        this.historicoPacientes = new ArbolPacientes();
    }

    public void registrarPaciente(Paciente p) {
        p.registrarCambio("Ingresado a Sala de Espera");

        colaAtencion.insertar(p); // inserta en la cola de prioridad
        salaEspera.put(p.getId(), p); // inserta en la sala de espera

        System.out.println("Paciente ingresado: " + p.getNombre());
    }

    public Paciente atenderSiguiente() {
        if (colaAtencion.estaVacia()) {
            System.out.println("No hay pacientes para atender");
            return null;
        }
        // elimino el paciente con mayor prioridad
        Paciente atendido = colaAtencion.eliminarMin();
        // elimino de la sala de espera
        salaEspera.remove(atendido.getId());
        // agrego al historico de pacientes
        atendido.registrarCambio("Atendido");
        historicoPacientes.insertar(atendido);
        // actualizo atendido
        System.out.println("Atendiendo a: " + atendido.getNombre());
        return atendido;
    }

    public Paciente buscarPacienteActivo(String id) {
        return salaEspera.get(id);
    }

    public Paciente buscarPacienteHistorico(String id) {
        return historicoPacientes.buscar(id);
    }

    // muestra el historico de pacientes atendidos
    public void mostrarHistorico() {
        List<Paciente> lista = historicoPacientes.obtenerPacientesEnOrden();
        System.out.println("--- Histórico ---");
        for (Paciente p : lista) {
            System.out.println(p);
        }
    }
}