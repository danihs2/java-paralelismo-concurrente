import java.util.LinkedList;
import java.util.Random;

// Clase Buffer que maneja la sincronización
class Buffer {
    private final LinkedList<Integer> buffer;
    private final int capacidad;

    public Buffer(int capacidad) {
        this.buffer = new LinkedList<>();
        this.capacidad = capacidad;
    }

    // Método sincronizado para agregar un elemento al buffer
    public synchronized void producir(int item) throws InterruptedException {
        while (buffer.size() == capacidad) {
            System.out.println("Buffer lleno. Esperando espacio...");
            wait(); // Esperar si el buffer está lleno
        }
        buffer.add(item);
        System.out.println("Producto generado: " + item);
        notifyAll(); // Notificar a los consumidores
    }

    // Método sincronizado para consumir un elemento del buffer
    public synchronized int consumir() throws InterruptedException {
        while (buffer.isEmpty()) {
            System.out.println("Buffer vacío. Esperando producción...");
            wait(); // Esperar si el buffer está vacío
        }
        int item = buffer.removeFirst();
        System.out.println("Producto consumido: " + item);
        notifyAll(); // Notificar a los productores
        return item;
    }
}

// Clase Productor que genera numeros aleatorios y los agrega al buffer
class Productor extends Thread {
    private final Buffer buffer;

    public Productor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            while (true) {
                int item = random.nextInt(100); // Genera un numero aleatorio
                buffer.producir(item);
                Thread.sleep(random.nextInt(1000)); // Simula el tiempo de producción
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Clase Consumidor que retira elementos del buffer y los procesa
class Consumidor extends Thread {
    private final Buffer buffer;

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int item = buffer.consumir();
                // Simula el procesamiento del producto
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Clase principal que configura el programa y crea hilos
public class ProductorConsumidor {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5); // Un buffer de tamaño 5

        // Crear hilos productores y consumidores
        Thread productor1 = new Productor(buffer);
        Thread productor2 = new Productor(buffer);
        Thread consumidor1 = new Consumidor(buffer);
        Thread consumidor2 = new Consumidor(buffer);

        // Iniciar los hilos
        productor1.start();
        productor2.start();
        consumidor1.start();
        consumidor2.start();
    }
}
