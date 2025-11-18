package estructura;

import modelos.Paciente;

public class NodoArbol {
    Paciente paciente;
    NodoArbol izquierdo;
    NodoArbol derecho;

    public NodoArbol(Paciente p) {
        this.paciente = p;
        this.izquierdo = null;
        this.derecho = null;
    }
}