import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class SumaMatricesMain {
    public static void main(String[] args) {
        // Tamaño de la matriz
        int filas = 4;
        int columnas = 4;

        int[][] matrizA = new int[filas][columnas];
        int[][] matrizB = new int[filas][columnas];
        int[][] resultado = new int[filas][columnas];

        // Inicializar las matrices con valores
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizA[i][j] = i + j;  // Valor arbitrario
                matrizB[i][j] = (i + 1) * (j + 1);  // Valor arbitrario
            }
        }

        // Crear el ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Crear y ejecutar la tarea de suma de matrices
        SumaMatricesTask tarea = new SumaMatricesTask(matrizA, matrizB, resultado, 0, filas);
        pool.invoke(tarea);

        // Mostrar el resultado
        System.out.println("Matriz A:");
        imprimirMatriz(matrizA);
        System.out.println("Matriz B:");
        imprimirMatriz(matrizB);
        System.out.println("Resultado (Suma de Matrices A y B):");
        imprimirMatriz(resultado);

        pool.shutdown();
    }

    // Método para imprimir una matriz
    private static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    static class SumaMatricesTask extends RecursiveTask<int[][]> {
        private int[][] matrizA;
        private int[][] matrizB;
        private int[][] resultado;
        private int inicioFila, finFila;
    
        private static final int THRESHOLD = 2; // Umbral para dividir las tareas 
    
        public SumaMatricesTask(int[][] matrizA, int[][] matrizB, int[][] resultado, int inicioFila, int finFila) {
            this.matrizA = matrizA;
            this.matrizB = matrizB;
            this.resultado = resultado;
            this.inicioFila = inicioFila;
            this.finFila = finFila;
        }
    
        @Override
        protected int[][] compute() {
            if (finFila - inicioFila <= THRESHOLD) {
                // Suma en el rango de filas determinado
                for (int i = inicioFila; i < finFila; i++) {
                    for (int j = 0; j < matrizA[i].length; j++) {
                        resultado[i][j] = matrizA[i][j] + matrizB[i][j];
                    }
                }
                return resultado;
            } else {
                // Dividir en dos tareas para procesar en paralelo
                int mitad = (inicioFila + finFila) / 2;
    
                SumaMatricesTask tarea1 = new SumaMatricesTask(matrizA, matrizB, resultado, inicioFila, mitad);
                SumaMatricesTask tarea2 = new SumaMatricesTask(matrizA, matrizB, resultado, mitad, finFila);
    
                // Invocar las tareas en paralelo
                invokeAll(tarea1, tarea2);
    
                // Combinar los resultados
                tarea1.join();
                tarea2.join();
    
                return resultado;
            }
        }
    }
    
}
