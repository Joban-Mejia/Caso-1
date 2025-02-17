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
            int intentos = 0; /* Contador para limitar los intentos de yield */

            // Espera semiactiva // corrección

            /* aqui uso waiten lugar de yield, toca revisalo despuies */
            synchronized (buzonRevision) {
                while ((producto = buzonRevision.retirar()) == null && !Main.finalizado) {
                    if (intentos < 10) {
                        Thread.yield();
                        intentos++;
                    } else { // 💤 Si después de 10 intentos sigue vacío, usamos `wait()`
                        synchronized (buzonRevision) {
                            try {
                                System.out.println(" El Equipo de calidad esta esperando productos...");
                                buzonRevision.wait(5000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                        if (Main.finalizado) return;
                    }
                }

                /* Si la simulacion acabo termina */
                if (Main.finalizado)
                    return;

                int resultadoRevision = random.nextInt(100) + 1;
                boolean esDefectuoso = resultadoRevision % 7 == 0;

                synchronized (this) {

                    if (esDefectuoso && fallosActuales < maxFallos) {
                        producto.setEstado(EstadoProducto.RECHAZADO);
                        fallosActuales++;
                        buzonReproceso.agregar(producto);
                        System.out.println(
                                "Producto rechazado ID = " + producto.getId() + " (Fallo #" + fallosActuales + ")");
                    }

                    else {
                        producto.setEstado(EstadoProducto.APROBADO);
                        deposito.agregar(producto);
                        System.out.println("Producto aprobado ID = " + producto.getId());

                        if (fallosActuales >= maxFallos) {
                            System.out.println("Límite de fallos alcanzado, aprobando todos los siguientes productos.");
                        }
                    }
                }
                /*
                 * Esto ya se hace en Deposito, por eso lo comento
                 * if (fallosActuales >= maxFallos) {
                 * synchronized (buzonReproceso) {
                 * System.out.println("Generando producto FIN y notificando a los hilos");
                 * Main.finalizado = true;
                 * buzonReproceso.agregar(new Producto(EstadoProducto.FIN));
                 * buzonReproceso.notifyAll();
                 * }
                 * synchronized (buzonRevision) {
                 * buzonRevision.notifyAll();
                 * }
                 * return;
                 *
                 * }
                 */
            }
        }
    }
}
