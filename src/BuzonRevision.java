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
            }
        }
        productos.add(producto);
        System.out.println("Producto agregado al buzón de revisión: ID=" + producto.getId());
        notifyAll();
    }

    public synchronized Producto retirar() {
        while (productos.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return productos.poll();
    }

    public synchronized boolean estaLleno() {
        return productos.size() >= capacidad;
    }
}
