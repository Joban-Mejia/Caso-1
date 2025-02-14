import java.util.LinkedList;
import java.util.Queue;

public class BuzonRevision {
    private final Queue<Producto> productos = new LinkedList<>();
    private final int capacidad;

    public BuzonRevision(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void agregar(Producto producto) {
        while (productos.size() >= capacidad) {
            try {
                System.out.println("Buzón de revisión lleno, esperando espacio...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; /* Sale si el hilo es interrumpido */
            }
        }
        productos.add(producto);
        System.out.println("Producto agregado al buzón de revisión: ID=" + producto.getId());
        notifyAll(); /* Despierta los hilos que esperan retirar */
    }

    public synchronized Producto retirar() {
        while (productos.isEmpty() && !Main.finalizado) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null; /* Sale si el hilo es interrumpido */
            }
            if (Main.finalizado && productos.isEmpty()) {
                notifyAll(); // Despierta a todos los hilos bloqueados
                return null; // Ya no hay más productos
            }
        }
        Producto producto = productos.poll();
        notifyAll(); /* Despierta los hilos que esperan agregar */
        return producto;

    }

    public synchronized boolean estaLleno() {
        return productos.size() >= capacidad;
    }
}
