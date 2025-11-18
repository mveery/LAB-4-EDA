package modelos;
import java.util.Stack;

public class Paciente implements Comparable<Paciente> {

    private String id;
    private String nombre;
    private int categoria;
    private long tiempoLlegada;
    private Stack<String> historialCambios;

    public Paciente(String id, String nombre, int categoria, long tiempoLlegada) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tiempoLlegada = tiempoLlegada;
        this.historialCambios = new Stack<>();
    }

    public void registrarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public long getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(long tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public Stack<String> getHistorialCambios() {
        return historialCambios;
    }

    public void setHistorialCambios(Stack<String> historialCambios) {
        this.historialCambios = historialCambios;
    }

    @Override
    public int compareTo(Paciente otro) {
        if (this.categoria < otro.categoria) {
            return -1;
        } else if (this.categoria > otro.categoria) {
            return 1;
        } else {
            if (this.tiempoLlegada < otro.tiempoLlegada) {
                return -1;
            } else if (this.tiempoLlegada > otro.tiempoLlegada) {
                return 1;
            } else {
                return 0; // solo si son idénticos en prioridad
            }
        }
    }

    @Override
    public String toString() {
        return "Paciente (RUT/Pasaporte: " + id + ", Nombre: " + nombre + 
            ", Categoria: C" + categoria + ", Tiempo_Llegada: " + tiempoLlegada + ")";
    }

}