import java.util.Random;

public class EquipoCalidad implements Runnable {
    private BuzonRevision buzonRevision;
    private BuzonReproceso buzonReproceso;
    private Deposito deposito;
    private int maxFallos;
    private int fallosActuales = 0;

    public EquipoCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, Deposito deposito, int maxFallos) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.maxFallos = maxFallos;
    }

    @Override
    public void run() {
        try {
            // Usamos un bucle controlado por el estado del producto
            boolean continuar = true;
            while (continuar) {
                Producto producto = buzonRevision.retirarProducto();

                // Verificar si el producto es de tipo FIN
                if (producto.getEstado() == EstadoProducto.FIN) {
                    continuar = false; // Salir del bucle
                } else {
                    boolean aprobado = revisarProducto(producto);
                    if (aprobado) {
                        deposito.almacenarProducto(producto);
                    } else {
                        buzonReproceso.agregarProducto(producto);
                        fallosActuales++;
                    }

                    // Verificar si se alcanzó el máximo de fallos
                    if (fallosActuales >= maxFallos) {
                        System.out.println("Máximo de fallos alcanzado. Todos los productos serán aprobados.");
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean revisarProducto(Producto producto) {
        Random rand = new Random();
        int numero = rand.nextInt(100) + 1;
        if (numero % 7 == 0 && fallosActuales < maxFallos) {
            return false;
        }
        return true;
    }
}