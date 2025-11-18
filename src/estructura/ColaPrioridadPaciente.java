package estructura;

import java.util.Arrays;
import java.util.NoSuchElementException;

import modelos.Paciente;

public class ColaPrioridadPaciente {
    private Paciente[] array;
    private int tamanio;
    private int capacidad;

    public ColaPrioridadPaciente() {
        this.capacidad = 100;
        this.tamanio = 0;
        this.array = new Paciente[capacidad];
    }

    // simular el arbol en un array
    private int getIndexPadre(int i) {
        return (i - 1) / 2;
    }

    private int getIndexHijoIzq(int i) {
        return 2 * i + 1;
    }

    private int getIndexHijoDer(int i) {
        return 2 * i + 2;
    }

    private boolean tienePadre(int i) {
        return i > 0;
    }

    private boolean tieneHijoIzq(int i) {
        return getIndexHijoIzq(i) < tamanio;
    }

    private boolean tieneHijoDer(int i) {
        return getIndexHijoDer(i) < tamanio;
    }

    private void capacidadMax() {
        if (tamanio == capacidad) {
            capacidad = capacidad * 2;
            array = Arrays.copyOf(array, capacidad);
        }
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public Paciente obtenerMin() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola de prioridad se encuentra vacía");
        }
        return array[0];
    }

    private void swap(int i, int j) {
        Paciente temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void siftUp(int index) {
        while (tienePadre(index) && array[index].compareTo(array[getIndexPadre(index)]) < 0) {
            // swap con el padre
            int indicePadre = getIndexPadre(index);
            swap(index, indicePadre);
            // sube al siguiente nivel y revisa
            index = indicePadre;
        }
    }

    private void siftDown(int index) {
        while (tieneHijoIzq(index)) {
            // se asume que el hijo izquierdo es el más prioritario
            int indiceHijoMenor = getIndexHijoIzq(index);
            // revisar si el hijo derecho es más prioritario
            if (tieneHijoDer(index) && array[getIndexHijoDer(index)].compareTo(array[indiceHijoMenor]) < 0) {
                // caso que si sea más prioritario
                indiceHijoMenor = getIndexHijoDer(index);
            }
            // comparar el padre con el hijo más prioritario
            if (array[index].compareTo(array[indiceHijoMenor]) < 0) {
                break;
            } else {
                // swap padre con hijo menor
                swap(index, indiceHijoMenor);
            }
            index = indiceHijoMenor;
        }
    }

    public void insertar(Paciente p) {
        capacidadMax();
        array[tamanio] = p; // aniado al final
        tamanio++;
        siftUp(tamanio - 1);
    }

    public Paciente eliminarMin() {
        Paciente raiz = obtenerMin();
        array[0] = array[tamanio - 1];
        tamanio--;
        siftDown(0); // arreglo mi heap
        return raiz;
    }
}
