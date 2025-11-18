package main;

import estructura.*;
import modelos.Hospital;
import modelos.Paciente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // tamaños de N para los experimentos
        int[] valorN = { 10000, 50000, 100000, 250000, 500000, 750000, 1000000 };
        System.out.println("==================================================");
        System.out.println("    EXPERIMENTOS - EDA LAB 4");
        System.out.println("==================================================\n");

        exp1(valorN);
        exp2();
        exp3(valorN);
        exp4();
    }

    private static void exp1(int[] valorN) {
        System.out.println("Experimento 1: Rendimiento Min-Heap (Cola de Prioridad)");
        System.out.printf("%-12s | %-20s | %-20s%n", "Tamaño (N)", "Inserción (ms)", "Extracción (ms)");
        System.out.println(String.join("", Collections.nCopies(60, "-")));

        for (int n : valorN) {
            Paciente[] datosPrueba = generarPacientes(n, false);
            ColaPrioridadPaciente cola = new ColaPrioridadPaciente();

            long startIns = System.nanoTime();
            for (Paciente p : datosPrueba) cola.insertar(p);
            double durIns = (System.nanoTime() - startIns) / 1_000_000.0;

            long startExt = System.nanoTime();
            while (!cola.estaVacia()) cola.eliminarMin();
            double durExt = (System.nanoTime() - startExt) / 1_000_000.0;

            System.out.printf("%-12d | %-20.3f | %-20.3f%n", n, durIns, durExt);
        }
        System.out.println("\n");
    }


}