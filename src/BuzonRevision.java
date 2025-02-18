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
                return;
            }
        }
        productos.add(producto);
        notifyAll();
        System.out.println("Producto aprobado ID = " + producto.getId());

    }


    
    public synchronized Producto retirar() {
        while (productos.isEmpty() && !Main.finalizado) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        if (Main.finalizado && productos.isEmpty()) {
            notifyAll();
            return null;
        }

        Producto producto = productos.poll();
        notifyAll();
        return producto;
    }

    public synchronized boolean estaLleno() {
        return productos.size() >= capacidad;
    }

    public synchronized void esperar() throws InterruptedException {
        while (estaLleno() && !Main.finalizado) { // Espera pasiva --- verifica FIN
            wait();
        }
    }


}
