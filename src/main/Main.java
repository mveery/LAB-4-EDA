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

    // metodo auxiliar para generar pacientes ordenados o aleatorios
    private static Paciente[] generarPacientes(int n, boolean ordenados) {
        Paciente[] arreglo = new Paciente[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            String id;
            if (ordenados) {
                int auxiliar = 10000000 + i; // mantiene orden y formato
                id = "ID-" + auxiliar;
            } else {
                id = "ID-" + i;
            }
            int categ = rand.nextInt(5) + 1;
            long tiempo = System.currentTimeMillis() + i;
            arreglo[i] = new Paciente(id, "Paciente " + i, categ, tiempo);
        }
        return arreglo;
    }

    // metodo auxiliar genera pacientes con ID desplazado
    private static Paciente[] generarPacientesDesde(int n, int offset) {
        Paciente[] arreglo = new Paciente[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int realIndex = offset + i;
            arreglo[i] = new Paciente("ID-" + realIndex, "Paciente " + realIndex, rand.nextInt(5) + 1,
                    System.currentTimeMillis() + i);
        }
        return arreglo;
    }

    // --- experimento 1 ----
    private static void exp1(int[] valorN) {
        System.out.println("Experimento 1: Rendimiento Min-Heap (Cola de Prioridad)");
        System.out.printf("%-12s | %-20s | %-20s%n", "Tamaño (N)", "Inserción (ms)", "Extracción (ms)");
        System.out.println(String.join("", Collections.nCopies(60, "-")));
        for (int k = 0; k < valorN.length; k++) {
            int n = valorN[k];
            Paciente[] datosPrueba = generarPacientes(n, false);
            ColaPrioridadPaciente cola = new ColaPrioridadPaciente();

            long inicioInsercion = System.nanoTime(); // tiempo inicio insercion
            for (int i = 0; i < datosPrueba.length; i++) {
                cola.insertar(datosPrueba[i]);
            }
            double duracionInsercion = (System.nanoTime() - inicioInsercion) / 1000000.0;

            long inicioExtraccion = System.nanoTime(); // tiempo inicio extraccion
            for (int i = 0; i < n; i++) {
                cola.eliminarMin();
            }
            double duracionExtraccion = (System.nanoTime() - inicioExtraccion) / 1000000.0;

            System.out.printf("%-12d | %-20.3f | %-20.3f%n", n, duracionInsercion, duracionExtraccion);
        }
        System.out.println("\n");
    }

    // --- experimento 2 ---
    private static void exp2() {
        System.out.println("Experimento 2: Rendimiento de Colisiones (Hash Fijo M=10007)");
        System.out.println("Generado de 1000 a 9000 pacientes, midiendo insercion y busqueda.");
        int M = 10007;
        HashPacientesEncadenado hashEncadenado = new HashPacientesEncadenado(M);
        HashPacientesSondeoLineal hashSondeoLineal = new HashPacientesSondeoLineal(M);
        List<Paciente> insertadosAcumulados = new ArrayList<>();

        int repeticiones = 50; // numero de veces a repetir busqueda para suavizar datos

        System.out.printf("%-12s | %-15s | %-15s | %-15s | %-15s%n",
                "Total (N)", "Insercion Encad(ms)", "Insercion Sond(ms)", "Busqueda Encad(ms)", "Busqueda Sond(ms)");
        System.out.println(String.join("", Collections.nCopies(85, "-")));

        // ciclo de 9 con 1000 pacientes cada uno
        for (int grupo = 1; grupo <= 9; grupo++) {
            int nGrupo = 1000;
            int totalPacientes = grupo * 1000;

            // genera nuevo grupo de pacientes
            Paciente[] grupoNuevo = generarPacientesDesde(nGrupo, (grupo - 1) * 1000);

            // mide insercion encadenamiento
            long t1 = System.nanoTime();
            for (int i = 0; i < grupoNuevo.length; i++) {
                hashEncadenado.put(grupoNuevo[i].getId(), grupoNuevo[i]);
            }
            double tInsEnc = (System.nanoTime() - t1) / 1000000.0;

            // mide insercion sondeo lineal
            long t2 = System.nanoTime();
            for (int i = 0; i < grupoNuevo.length; i++) {
                hashSondeoLineal.put(grupoNuevo[i].getId(), grupoNuevo[i]);
            }
            double tInsSon = (System.nanoTime() - t2) / 1000000.0;

            Collections.addAll(insertadosAcumulados, grupoNuevo);

            // genera 1000 IDs aleatorios para busqueda
            List<String> idsABuscar = new ArrayList<>();
            Random rand = new Random();
            for (int k = 0; k < 1000; k++) {
                Paciente pRandom = insertadosAcumulados.get(rand.nextInt(insertadosAcumulados.size()));
                idsABuscar.add(pRandom.getId());
            }

            // mide busqueda encadenamiento (promedio 50 veces)
            long sumaTiempoEnc = 0;
            for (int r = 0; r < repeticiones; r++) {
                long t3 = System.nanoTime();
                for (int i = 0; i < idsABuscar.size(); i++) {
                    hashEncadenado.get(idsABuscar.get(i));
                }
                sumaTiempoEnc += (System.nanoTime() - t3);
            }
            double tBusEnc = (sumaTiempoEnc / (double) repeticiones) / 1000000.0;

            // mide busqueda sondeo lineal (promedio 50 veces)
            long sumaTiempoSon = 0;
            for (int r = 0; r < repeticiones; r++) {
                long t4 = System.nanoTime();
                for (int i = 0; i < idsABuscar.size(); i++) {
                    hashSondeoLineal.get(idsABuscar.get(i));
                }
                sumaTiempoSon += (System.nanoTime() - t4);
            }
            double tBusSon = (sumaTiempoSon / (double) repeticiones) / 1000000.0;

            System.out.printf("%-12d | %-15.3f | %-15.3f | %-15.3f | %-15.3f%n",
                    totalPacientes, tInsEnc, tInsSon, tBusEnc, tBusSon);
        }
        System.out.println("\n");
    }

    // --- experimento 3 ---
    private static void exp3(int[] valorN) {
        System.out.println("Experimento 3: Búsqueda Hash O(1) vs BST O(log n) vs BST O(n)");
        System.out.println("Tiempo de buscar 10,000 elementos en cada estructura (promedio 50 repeticiones)");
        System.out.printf("%-12s | %-15s | %-15s | %-15s%n", "Tamaño (N)", "Hash (ms)", "BST prom(ms)", "BST Peor(ms)");
        System.out.println(String.join("", Collections.nCopies(70, "-")));

        int repeticiones = 50; // numero de veces a repetir para suavizar datos

        for (int k = 0; k < valorN.length; k++) {
            int n = valorN[k];
            int cantidadBusqueda = 10000;
            // ajusta si n es menor a 10,000
            if (n < cantidadBusqueda) {
                cantidadBusqueda = n;
            }

            // 1: datos aleatorios para caso promedio
            Paciente[] datosRandom = generarPacientes(n, false);
            // preparo hash, bst, para caso promedio
            HashPacientesEncadenado hash = new HashPacientesEncadenado(10007);
            for (int i = 0; i < datosRandom.length; i++) {
                hash.put(datosRandom[i].getId(), datosRandom[i]);
            }
            ArbolPacientes bstPromedio = new ArbolPacientes();
            for (int i = 0; i < datosRandom.length; i++) {
                bstPromedio.insertar(datosRandom[i]);
            }

            // mido hash (promedio de 50 veces)
            long sumaTiempoHash = 0;
            for (int r = 0; r < repeticiones; r++) {
                long inicioHash = System.nanoTime();
                for (int i = 0; i < cantidadBusqueda; i++) {
                    hash.get(datosRandom[i].getId());
                }
                sumaTiempoHash += (System.nanoTime() - inicioHash);
            }
            double duracionHash = (sumaTiempoHash / (double) repeticiones) / 1000000.0;

            // mido bst promedio (promedio de 50 veces)
            long sumaTiempoBST = 0;
            for (int r = 0; r < repeticiones; r++) {
                long inicioBSTProm = System.nanoTime();
                for (int i = 0; i < cantidadBusqueda; i++) {
                    bstPromedio.buscar(datosRandom[i].getId());
                }
                sumaTiempoBST += (System.nanoTime() - inicioBSTProm);
            }
            double duracionBSTProm = (sumaTiempoBST / (double) repeticiones) / 1000000.0;

            // 2: datos ordenados para caso peor bst
            double duracionBSTPeor = 0.0;
            boolean sePudoMedir = false;

            // con n limitado a 20,000 para evitar overflow
            if (n <= 20000) {
                Paciente[] datosOrdenados = generarPacientes(n, true);
                ArbolPacientes bstPeor = new ArbolPacientes();
                // inserta ordenadamente
                for (int i = 0; i < datosOrdenados.length; i++) {
                    bstPeor.insertar(datosOrdenados[i]);
                }
                // mide bst peor caso
                long inicioBSTPeor = System.nanoTime();
                for (int i = 0; i < cantidadBusqueda; i++) {
                    bstPeor.buscar(datosOrdenados[i].getId());
                }
                duracionBSTPeor = (System.nanoTime() - inicioBSTPeor) / 1000000.0;
                sePudoMedir = true;
            }

            if (sePudoMedir) {
                System.out.printf("%-12d | %-15.3f | %-15.3f | %-15.3f%n",
                        n, duracionHash, duracionBSTProm, duracionBSTPeor);
            } else {
                // n muy grande, no se pudo medir bst peor
                System.out.printf("%-12d | %-15.3f | %-15.3f | %-15s%n",
                        n, duracionHash, duracionBSTProm, "Muy Lento");
            }
        }
        System.out.println("(obs: Para N > 20k, el peor caso excede la memoria de la recursion)\n");
    }

    // --- experimento 4 ---
    private static void exp4() {
        System.out.println("Experimento 4: Simulación de Urgencia (Semilla 42)");
        System.out.println("==========================================================\n");

        // 1: flujo normal: llegada cada 10m, atencion cada 15m
        System.out.println(">>> Escenario 1: Flujo Normal, 144 llegadas, 96 atendidos (lleg 10m vs aten 15m)");
        simularEscenario(10, 15);

        System.out.println("\n" + String.join("", Collections.nCopies(60, "-")) + "\n");

        // 2: sobrecarga: llegada cada 8m, atencion cada 15m
        System.out.println(">>> Escenario 2: Alta Sobrecarga, 180 llegadas, 96 atendidos (lleg 8m vs aten 15m)");
        simularEscenario(8, 15);
    }

    // aux genera categoria con semilla dada, con probalidades especificas
    private static int generarCategoriaConSeed(Random rand) {
        int r = rand.nextInt(100) + 1; // 1 a 100
        if (r <= 10)
            return 1;
        if (r <= 25)
            return 2;
        if (r <= 43)
            return 3;
        if (r <= 70)
            return 4;
        return 5;
    }

    //aux simular escenario con tasas dadas, para implementar en exp4
    private static void simularEscenario(int tasaLlegada, int tasaAtencion) {
        // seed 42 para reproducibilidad
        Random randSimulacion = new Random(42);
        Hospital hospital = new Hospital(500);
        int minutosDia = 1440;
        // contador de pacientes para IDs en secuencia
        int contadorPacientes = 0; 
        long[] sumaTiemposEspera = new long[5];
        int[] contAtendidos = new int[5];
        int[] contLlegadas = new int[5];

        // simulacion minuto a minuto
        for (int t = 0; t < minutosDia; t++) {
            // llega un paciente cada "tasaLlegada" minutos correspondientes
            if (t % tasaLlegada == 0) {
                contadorPacientes++; // suma 1 al contador en secuencia
                int categ = generarCategoriaConSeed(randSimulacion);
                // usa el contador en secuencia para el ID y nombre
                Paciente p = new Paciente("ID-" + contadorPacientes, "Paciente-" + contadorPacientes, categ, t);
                hospital.registrarPaciente(p);
                contLlegadas[categ - 1]++;
            }

            // atender un paciente cada "tasaAtencion" minutos correspondientes
            if (t > 0 && t % tasaAtencion == 0) {
                Paciente atendido = hospital.atenderSiguiente();
                if (atendido != null) {
                    long tiempoEspera = t - atendido.getTiempoLlegada();
                    int cat = atendido.getCategoria() - 1;
                    sumaTiemposEspera[cat] += tiempoEspera; // acumula espera
                    contAtendidos[cat]++; // contador por categoria
                }
            }
        }

        // datos finales del escenario...
        System.out.println("\n--- Resultados del Escenario ---");
        System.out.printf("%-5s | %-10s | %-10s | %-15s | %-15s%n",
                "Categ", "Llegaron", "Atendidos", "En Espera", "Espera Prom(min)");
        System.out.println(String.join("", Collections.nCopies(70, "-")));
        int totalLlegaron = 0;
        int totalAtendidos = 0;
        int totalEsperando = 0;
        // recorre categorias
        for (int c = 1; c <= 5; c++) {
            int index = c - 1; // índice ajustado
            int esperando = contLlegadas[index] - contAtendidos[index];
            double promedio = 0.0;
            // calcula promedio de espera
            if (contAtendidos[index] > 0) { // evita division por 0
                promedio = (double) sumaTiemposEspera[index] / contAtendidos[index];
            }
            // acumula totales
            totalLlegaron += contLlegadas[index];
            totalAtendidos += contAtendidos[index];
            totalEsperando += esperando;
            System.out.printf("C%-4d | %-10d | %-10d | %-15d | %-15.2f%n",
                    c, contLlegadas[index], contAtendidos[index], esperando, promedio);
        }
        System.out.println(String.join("", Collections.nCopies(70, "-")));
        System.out.printf("TOTAL  | %-10d | %-10d | %-15d | - %n",
                totalLlegaron, totalAtendidos, totalEsperando);
    }

}