import java.util.LinkedList;
import java.util.Queue;

public class BuzonReproceso {
    private final Queue<Producto> productos = new LinkedList<>();

    public synchronized void agregar(Producto producto) {
        productos.add(producto);
        System.out.println("Producto enviado a reproceso: ID=" + producto.getId());
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

    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }
}
