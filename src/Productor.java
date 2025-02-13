public class Productor implements Runnable {
    private BuzonReproceso buzonReproceso;
    private BuzonRevision buzonRevision;
    private int numProductos;

    public Productor(BuzonReproceso buzonReproceso, BuzonRevision buzonRevision, int numProductos) {
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
        this.numProductos = numProductos;
    }

    @Override
    public void run() {
        try {
            int productosProducidos = 0;
            
            while (productosProducidos < numProductos) {
                if (!buzonReproceso.estaVacio()) 
                {

                    // Reprocesar un producto del buzón de reproceso
                    Producto producto = buzonReproceso.retirarProducto();
                    producto.setEstado(EstadoProducto.REPROCESADO);
                    buzonRevision.agregarProducto(producto);
                    System.out.println("SE HA REPROCESADO EL PRODUCTO" + producto); //TODO No está  entrando, no está reprocesando
                
                } else {
                    
                    Producto producto = new Producto(EstadoProducto.NUEVO);
                    buzonRevision.agregarProducto(producto);
                    Thread.sleep(10000); //simulado de tiempo de producción (temporal) 
                    System.out.println("SE HA CREADO UN PRODUCTO (CLASE PRODUCTOR FUNCIONANDO) ----" + producto);

                }

            
            productosProducidos++;
            Thread.sleep(1000);
            
            }
            System.out.println("El productor ha terminado de producir " + numProductos + " productos.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}