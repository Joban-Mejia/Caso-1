public class Productor extends Thread {
    private final BuzonReproceso buzonReproceso;
    private final BuzonRevision buzonRevision;
    private final int totalProductos;
    private static int productosGenerados = 0;

    public Productor(BuzonReproceso buzonReproceso, BuzonRevision buzonRevision, int totalProductos) {
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
        this.totalProductos = totalProductos;
    }

    @Override
    public void run() {
        while (!Main.finalizado) {
            Producto producto;

            synchronized (buzonReproceso) {
                while (buzonReproceso.estaVacio() && productosGenerados >= totalProductos) {
                    try {
                        buzonReproceso.wait(); //Espera pasiva --corrección
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (!buzonReproceso.estaVacio()) {
                    producto = buzonReproceso.retirar();
                    if (producto.getEstado() == EstadoProducto.FIN) {
                        System.out.println("Productor recibe producto FIN, terminando ejecución...");
                        
                        synchronized (buzonReproceso) {
                            Main.finalizado = true;
                            buzonReproceso.notifyAll();  
                        }
                        synchronized (buzonRevision) {
                            buzonRevision.notifyAll();  
                        }
                        return;

                    }
                    producto.setEstado(EstadoProducto.REPROCESADO);
                    System.out.println("Reprocesando producto ID=" + producto.getId());} 
                    
                    else { synchronized (Productor.class) {
                        if (productosGenerados >= totalProductos) {
                            return;
                        }
                        producto = new Producto(EstadoProducto.NUEVO);
                        productosGenerados++;
                    }
                }
            }

            synchronized (buzonRevision) {
                while (buzonRevision.estaLleno()) {
                    try {
                        System.out.println("Buzón de revisión lleno, esperando espacio...");
                        buzonRevision.wait(); // espera pasiva 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                buzonRevision.agregar(producto);
                buzonRevision.notifyAll();
        }
    }
}
}