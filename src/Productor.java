public class Productor implements Runnable {
    private BuzonReproceso buzonReproceso;
    private BuzonRevision buzonRevision;
    private int numProductos;
    int productosGenerados = 0;

    public Productor(BuzonReproceso buzonReproceso, BuzonRevision buzonRevision, int numProductos) {
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
        this.numProductos = numProductos;
    }

    @Override
    public void run() {
        try {
            while (productosGenerados < numProductos) {
                if (!buzonReproceso.estaVacio()) {
                    Producto producto = buzonReproceso.retirarProducto();
                    producto.setEstado(EstadoProducto.REPROCESADO);
                    buzonRevision.agregarProducto(producto);
                    System.out.println("=============================================================El producto es" + producto);
                } else {
                    Producto producto = new Producto(EstadoProducto.NUEVO);
                    buzonRevision.agregarProducto(producto);
                }

                // Verificar si se ha alcanzado el número de productos requeridos
                if (buzonRevision.getProductosAlmacenados() >= numProductos) {
                    System.out.println("==============================El número de productos requeridos fue alcanzado");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}