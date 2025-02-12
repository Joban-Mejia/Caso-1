import java.util.Queue;
import java.util.LinkedList;

public class BuzonReproceso {
    private Queue<Producto> productos = new LinkedList<>();
    private final int capacidad;

    public BuzonReproceso(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void agregarProducto(Producto producto) throws InterruptedException {
        while (productos.size() >= capacidad) {
            wait();
        }
        productos.add(producto);
        notifyAll();
    }

    public synchronized Producto retirarProducto() throws InterruptedException {
        while (productos.isEmpty()) {
            wait();
        }
        Producto producto = productos.poll();
        notifyAll();
        return producto;
    }

    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }
}