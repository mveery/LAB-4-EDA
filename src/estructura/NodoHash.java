package estructura;

import modelos.Paciente;

public class NodoHash {
    String key;
    Paciente valor;
    
    public NodoHash(String key, Paciente valor) {
        this.key = key;
        this.valor = valor;
    }
}