package estructura;

import modelos.Paciente;
import java.util.ArrayList;
import java.util.List;

public class ArbolPacientes {
    private NodoArbol raiz;

    public ArbolPacientes() {
        this.raiz = null;
    }

    // inserta un paciente en el arbol, con el metodo recursivo
    public void insertar(Paciente p) {
        raiz = insertarRecursivo(raiz, p);
    }

    // metodo recursivo para insertar
    private NodoArbol insertarRecursivo(NodoArbol actual, Paciente p) {
        // caso base, si es null crea un nuevo nodo
        if (actual == null) {
            return new NodoArbol(p);
        }
        // caso recursivo, compara y decide ir a izquierda o derecha
        // compareTo menor a 0 izquierda, mayor a 0 derecha, igual a 0 nada
        if (p.getId().compareTo(actual.paciente.getId()) < 0) {
            actual.izquierdo = insertarRecursivo(actual.izquierdo, p);
        }

        else if (p.getId().compareTo(actual.paciente.getId()) > 0) {
            actual.derecho = insertarRecursivo(actual.derecho, p);
        }

        return actual;
    }

    // busca un paciente por ID, con metodo recursivo
    public Paciente buscar(String id) {
        return buscarRec(raiz, id);
    }

    private Paciente buscarRec(NodoArbol actual, String id) {
        // caso base, no encontrado
        if (actual == null) {
            return null;
        }
        // caso base, encontrado
        if (id.equals(actual.paciente.getId())) {
            return actual.paciente;
        }

        // recursividad para buscar en subarboles
        if (id.compareTo(actual.paciente.getId()) < 0) {
            return buscarRec(actual.izquierdo, id);
        } else {
            return buscarRec(actual.derecho, id);
        }
    }

    // obtener lista de pacientes en orden (in-order traversal)
    public List<Paciente> obtenerPacientesEnOrden() {
        List<Paciente> lista = new ArrayList<>();
        inOrderRec(raiz, lista);
        return lista;
    }

    private void inOrderRec(NodoArbol nodo, List<Paciente> lista) {
        if (nodo != null) {
            inOrderRec(nodo.izquierdo, lista); // revisa izquierda (menores)
            lista.add(nodo.paciente); // revisa raiz (actual)
            inOrderRec(nodo.derecho, lista); // revisa derecha (mayores)
        }
    }
}