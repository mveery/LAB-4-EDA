package estructura;

import modelos.Paciente;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class HashPacientesSondeoLineal implements Map<String, Paciente> {
    private Paciente[] tabla;
    // auxiliar para manejo de borrado 
    // true cuando tuvo un dato que fue borrado, continuo busqueda
    private boolean[] isDeleted;
    private int M;
    private int size;

    public HashPacientesSondeoLineal(int capacidad) {
        this.M = capacidad;
        this.tabla = new Paciente[M];
        this.isDeleted = new boolean[M];
        this.size = 0;
    }

    // funcion hash para strings
    private int hash(Object key) {
        String id = (String) key;
        int h = 0;
        for (int i = 0; i < id.length(); i++) {
            h = 31 * h + id.charAt(i);
        }
        return Math.abs(h % M);
    }

    @Override
    public Paciente put(String key, Paciente value) {
        if (size == M) {
            throw new RuntimeException("Tabla llena: No se puede insertar mas con Sondeo Lineal");
        }

        int index = hash(key);
        int i = 0;
        // while mientras no encontremos un espacio vacio, con formula circular
        while (tabla[(index + i) % M] != null) {
            int actual = (index + i) % M;
            // getID para comparar "strings"
            if (tabla[actual].getId().equals(key)) {

                // si se encuentra la key, actualiza el valor
                if (isDeleted[actual]) {
                    // si estaba "borrado" antes, ahora se reinserta
                    isDeleted[actual] = false;
                    size++; 
                    tabla[actual] = value;
                    return null;
                } else {
                    // si estaba activo, actualiza normalmente
                    Paciente antiguo = tabla[actual];
                    tabla[actual] = value;
                    return antiguo;
                }
            }
            i++; // linear probing, siguiente casilla
        }

        // en caso de encontrar un espacio libre
        int insertIndex = (index + i) % M;
        tabla[insertIndex] = value; //inserta el nuevo paciente
        isDeleted[insertIndex] = false; // con auxiliar, marca como activo
        size++;
        return null; // retorna null, no habia valor previo
    }

    @Override
    public Paciente get(Object key) {
        String id = (String) key;
        int index = hash(id);
        int i = 0;
        // busqueda, mientras no encontremos un espacio vacio
        while (tabla[(index + i) % M] != null) {
            int actual = (index + i) % M;

            // si encuentra la key
            if (tabla[actual].getId().equals(id)) {
                // maneja si lo encuentra borrado o no, sigue buscando
                if (!isDeleted[actual]) {
                    return tabla[actual]; //retorna el paciente si no esta borrado
                }
            }
            i++;
            if (i == M) return null; // evita while infinito
        }
        return null; // paciente no existe
    }

    @Override
    public Paciente remove(Object key) {
        String id = (String) key;
        int indice = hash(id);
        int i = 0;
        // busqueda, mientras no encontremos un espacio vacio
        while (tabla[(indice + i) % M] != null) {
            int actual = (indice + i) % M;
            if (tabla[actual].getId().equals(id)) {
                if (isDeleted[actual]){
                    return null; // ya estaba borrado
                }
                // lo marca como borrado, no lo elimina fisicamente
                isDeleted[actual] = true;
                size--;
                return tabla[actual];
            }
            i++;
            if (i == M) return null;
        }
        return null;
    }


    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < M; i++) {
            tabla[i] = null;
            isDeleted[i] = false;
        }
        size = 0;
    }

    @Override public boolean containsKey(Object key) {
        return get(key) != null; 
    }


    //metodos no implementados
    @Override public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Método no implementado."); 
    }

    @Override public void putAll(Map<? extends String, ? extends Paciente> m) {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override public Set<String> keySet() {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override public Collection<Paciente> values() {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override public Set<Entry<String, Paciente>> entrySet() { 
        throw new UnsupportedOperationException("Método no implementado."); 
    }
}