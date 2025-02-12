import java.util.Queue;
import java.util.LinkedList;

public class BuzonRevision {
    private Queue<Producto> productos = new LinkedList<>();
    private final int capacidad;

    public BuzonRevision(int capacidad) {
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

    public synchronized int getProductosAlmacenados() {
        return productos.size();
    }
}