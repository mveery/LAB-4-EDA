package estructura;

import modelos.Paciente;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class HashPacientesEncadenado implements Map<String, Paciente> {

    private LinkedList<NodoHash>[] tabla;
    private int M;
    private int size;

    @SuppressWarnings("unchecked") // necesario para arreglo generico con LinkedList<NodoHash>[]
    public HashPacientesEncadenado(int capacidad) {
        this.M = capacidad;
        this.size = 0;
        this.tabla = new LinkedList[M];
        // inicializo cada lista en el arreglo
        for (int i = 0; i < M; i++) {
            tabla[i] = new LinkedList<>();
        }
    }

    private int hash(Object key) {
        String id = (String) key;
        int h = 0;
        // algoritmo para strings a numero en hash
        for (int i = 0; i < id.length(); i++) {
            h = 31 * h + id.charAt(i);
        }
        return Math.abs(h % M); // fuerza el absoluto para evitar indices negativos
    }

    @Override
    public Paciente put(String key, Paciente value) {
        int index = hash(key);

        // busca si ya existe en la lista, for each para mayor eficiencia
        for (NodoHash nodo : tabla[index]) {
            if (nodo.key.equals(key)) {
                Paciente antiguo = nodo.valor;
                nodo.valor = value;
                return antiguo; // map exige devolver el valor anterior
            }
        }

        // encadenamiento, agrega al final de la lista si no existe
        tabla[index].add(new NodoHash(key, value));
        size++;
        return null; // retorna null, no habia valor previo
    }

    @Override
    public Paciente get(Object key) {
        int index = hash(key);
        // busca en la lista correspondiente
        for (NodoHash nodo : tabla[index]) {
            if (nodo.key.equals(key)) { // reviso si existe la key
                return nodo.valor;
            }
        }
        return null;
    }

    @Override
    public Paciente remove(Object key) {
        int index = hash(key);

        for (NodoHash nodo : tabla[index]) {
            if (nodo.key.equals(key)) {
                Paciente eliminado = nodo.valor;
                tabla[index].remove(nodo); // elimino el nodo de la lista
                size--;
                return eliminado;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < M; i++) {
            tabla[i].clear();
        }
        size = 0;
    }

    //metodos no implementados

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Paciente> m) {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override
    public Collection<Paciente> values() {
        throw new UnsupportedOperationException("Método no implementado.");
    }

    @Override
    public Set<Entry<String, Paciente>> entrySet() {
        throw new UnsupportedOperationException("Método no implementado.");
    }
}