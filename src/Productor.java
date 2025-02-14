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
                if (!buzonReproceso.estaVacio()) {
                    producto = buzonReproceso.retirar();
                    if (producto.getEstado() == EstadoProducto.FIN) {
                        System.out.println("Productor recibe producto FIN, terminando...");
                        Main.finalizado = true;
                        break;
                    }
                    producto.setEstado(EstadoProducto.REPROCESADO);
                    System.out.println("Reprocesando producto ID=" + producto.getId());
                } else {
                    synchronized (Productor.class) {
                        if (productosGenerados >= totalProductos) {
                            break; // Ya se generaron todos los productos necesarios
                        }
                        producto = new Producto(EstadoProducto.NUEVO);
                        productosGenerados++;
                    }
                }
            }

            buzonRevision.agregar(producto);
        }
    }
}
