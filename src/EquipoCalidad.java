import java.util.Random;

public class EquipoCalidad extends Thread {
    private final BuzonRevision buzonRevision;
    private final BuzonReproceso buzonReproceso;
    private final Deposito deposito;
    private final int maxFallos;
    private int fallosActuales = 0;
    private final Random random = new Random();

    public EquipoCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, Deposito deposito,
                         int totalProductos) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.maxFallos = (int) Math.floor(totalProductos * 0.10);
    }

    @Override
    public void run() {
        while (!Main.finalizado) {
            Producto producto;

            // Espera semiactiva con yield()
            while ((producto = buzonRevision.retirar()) == null && !Main.finalizado) {
                Thread.yield(); 
            }

            //termina de ejecutar si terminó
            if (Main.finalizado)
                return;

            int resultadoRevision = random.nextInt(100) + 1;
            boolean esDefectuoso = resultadoRevision % 7 == 0;

            synchronized (this) {
                if (esDefectuoso && fallosActuales < maxFallos) {
                    producto.setEstado(EstadoProducto.RECHAZADO);
                    buzonReproceso.agregar(producto);
                    System.out.println("Producto rechazado ID = " + producto.getId());
                } else {
                    producto.setEstado(EstadoProducto.APROBADO);
                    deposito.agregar(producto);
                    System.out.println("Producto aprobado ID = " + producto.getId());

                    if (fallosActuales >= maxFallos) {
                        System.out.println("Límite de fallos alcanzado, aprobando todos los siguientes productos.");
                    }
                }
            }
        }
    }
}
