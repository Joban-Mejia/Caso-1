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
        Deposito deposito = new Deposito(numProductos);

        List<Thread> hilos = new ArrayList<>();

        //Crea un thread por cada productor
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
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Simulación terminada");
    }
}
