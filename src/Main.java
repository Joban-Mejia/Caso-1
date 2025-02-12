import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de operarios: ");
        int numOperarios = scanner.nextInt();

        System.out.print("Ingrese el número de productos a producir: ");
        int numProductos = scanner.nextInt();

        System.out.print("Ingrese el límite del buzón de revisión: ");
        int limiteBuzonRevision = scanner.nextInt();


        BuzonReproceso buzonReproceso = new BuzonReproceso(1000); // Capacidad del buzón de reproceso
        
        BuzonRevision buzonRevision = new BuzonRevision(limiteBuzonRevision);
        Deposito deposito = new Deposito(numProductos);

        int maxFallos = (int) Math.floor(numProductos * 0.1);

        for (int i = 0; i < numOperarios; i++) {
            new Thread(new Productor(buzonReproceso, buzonRevision, numProductos)).start();
            new Thread(new EquipoCalidad(buzonRevision, buzonReproceso, deposito, maxFallos)).start();
        }
    }
}