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
            while (true) {
                if (!buzonReproceso.estaVacio()) {
                    Producto producto = buzonReproceso.retirarProducto();
                    producto.setEstado(EstadoProducto.REPROCESADO);
                    buzonRevision.agregarProducto(producto);
                } else {
                    Producto producto = new Producto(EstadoProducto.NUEVO);
                    buzonRevision.agregarProducto(producto);
                }

                // Verificar si se ha alcanzado el número de productos requeridos
                if (buzonRevision.getProductosAlmacenados() >= numProductos) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}