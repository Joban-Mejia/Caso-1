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

        BuzonReproceso buzonReproceso = new BuzonReproceso();
        BuzonRevision buzonRevision = new BuzonRevision(capacidadBuzon);
        Deposito deposito = new Deposito(numProductos, buzonReproceso);

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
                hilo.join(5000); /* Esperar hasta 5 segundos por cada hilo */
                if (hilo.isAlive()) {
                    System.out.println("El hilo " + hilo.getName() + " sigue vivo, forzando finalización...");
                    finalizado = true; /* Marcar finalizado si algún hilo sigue vivo */
                    synchronized (buzonRevision) {
                        buzonRevision.notifyAll(); /* Despertar hilos bloqueados */
                    }
                    synchronized (buzonReproceso) {
                        buzonReproceso.notifyAll();  /* Despertar hilos bloqueados */
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        //Imprimir estado final de los hilos
        System.out.println("\n=== ESTADO FINAL DE LOS HILOS ===");
        Thread.getAllStackTraces().keySet().forEach(t -> {
            if (t.getName().startsWith("Thread-")) {
                System.out.println("Thread: " + t.getName() + " - Estado: " + t.getState());
            }
        });

        System.out.println("Todos los hilos de la simulación han finalizado.");
        System.out.println("Simulación terminada.");
    }
}
