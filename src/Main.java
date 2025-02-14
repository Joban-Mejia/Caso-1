import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static volatile boolean finalizado = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de operarios: ");
        int numOperarios = scanner.nextInt();

        System.out.print("Ingrese el número de productos a producir: ");
        int numProductos = scanner.nextInt();

        System.out.print("Ingrese la capacidad del buzón de revisión: ");
        int capacidadBuzon = scanner.nextInt();

        scanner.close();
        Thread.getAllStackTraces().keySet()
                .forEach(t -> System.out
                        .println("=================================Thread activo:================================= "
                                + t.getName() + " - Estado: " + t.getState()));

        BuzonReproceso buzonReproceso = new BuzonReproceso();
        BuzonRevision buzonRevision = new BuzonRevision(capacidadBuzon);
        Deposito deposito = new Deposito(numProductos);

        List<Thread> hilos = new ArrayList<>();

        // Crea un thread por cada productor
        for (int i = 0; i < numOperarios; i++) {
            Productor productor = new Productor(buzonReproceso, buzonRevision, numProductos);
            hilos.add(productor);
            productor.start();
        }

        // Crea un thread por cada operario de equipo de calidad
        for (int i = 0; i < numOperarios; i++) {
            EquipoCalidad equipoCalidad = new EquipoCalidad(buzonRevision, buzonReproceso, deposito, numProductos);
            hilos.add(equipoCalidad);
            equipoCalidad.start();
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join(5000); // Espera un máximo de 5 segundos
                if (hilo.isAlive()) {
                    System.out.println("El hilo " + hilo.getName() + " sigue vivo, forzando finalización...");
                    Main.finalizado = true;
                    synchronized (buzonRevision) {
                        buzonRevision.notifyAll(); /* Despierta hilos bloqueados */
                    }
                    synchronized (buzonReproceso) {
                        buzonReproceso.notifyAll(); /* Asegura que ningún hilo quede atascado */
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        /* Lo que hace este print es mostrarnos si alguno de los hilos que
         * nosotros creamos siguen activos*/

        /* System.out.println("\n=== ESTADO FINAL DE LOS HILOS CREADOS POR LA SIMULACIÓN ===");
        Thread.getAllStackTraces().keySet().forEach(t -> {
            if (t.getName().startsWith("Thread-")) { // Filtra solo los hilos creados dinámicamente
                System.out.println("Thread: " + t.getName() + " - Estado: " + t.getState());
            }
        });
        System.out.println("Todos los hilos de la simulación han finalizado."); */

        System.out.println("Simulación terminada");
    }
}
