import java.util.LinkedList;
import java.util.Queue;

public class BuzonReproceso {
    private final Queue<Producto> productos = new LinkedList<>();

    public synchronized void agregar(Producto producto) {
        productos.add(producto);
        notifyAll();
        System.out.println("Producto enviado a reproceso: ID=" + producto.getId());
    }

    public synchronized Producto retirar() {
        while (productos.isEmpty() && !Main.finalizado) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }

            if (Main.finalizado && productos.isEmpty()) {
                notifyAll();  /* Despertanddo a todos los hilos bloqueados antes de salir */
                return null;
            }

        }
        return productos.poll();
    }

    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }
}
