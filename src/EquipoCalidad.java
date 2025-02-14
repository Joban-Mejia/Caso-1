import java.util.Random;

public class EquipoCalidad extends Thread {
    private final BuzonRevision buzonRevision;
    private final BuzonReproceso buzonReproceso;
    private final Deposito deposito;
    private final int maxFallos;
    private int fallosActuales = 0;
    private final Random random = new Random();

    public EquipoCalidad(BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, Deposito deposito, int totalProductos) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.maxFallos = (int) Math.floor(totalProductos * 0.10);
    }

    @Override
    public void run() {
        while (!Main.finalizado) {
            Producto producto = buzonRevision.retirar();

            if (producto == null) continue;

            int resultadoRevision = random.nextInt(100) + 1;
            boolean esDefectuoso = resultadoRevision % 7 == 0;

            if (esDefectuoso && fallosActuales < maxFallos) {
                producto.setEstado(EstadoProducto.RECHAZADO);
                fallosActuales++;
                buzonReproceso.agregar(producto);
                System.out.println("Producto rechazado ID=" + producto.getId() + " (Fallo #" + fallosActuales + ")");
            } else {
                producto.setEstado(EstadoProducto.APROBADO);
                deposito.agregar(producto);
                System.out.println("Producto aprobado ID=" + producto.getId());

                if (fallosActuales >= maxFallos) {
                    System.out.println("Límite de fallos alcanzado, aprobando todos los siguientes productos.");
                }
            }

            if (fallosActuales >= maxFallos) {
                Producto fin = new Producto(EstadoProducto.FIN);
                buzonReproceso.agregar(fin);
                System.out.println("Equipo de calidad envía FIN. Terminando...");
                Main.finalizado = true; 
                break;
            }
        }
    }
}
