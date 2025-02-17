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
            Producto producto = null;

            synchronized (buzonReproceso) {
                if (!buzonReproceso.estaVacio()) {
                    producto = buzonReproceso.retirar();
                    if (producto != null && producto.getEstado() == EstadoProducto.FIN) {
                        System.out.println("Productor recibe producto FIN, terminando ejecución...");

                        Main.finalizado = true;

                        synchronized (buzonReproceso) {
                            buzonReproceso.notifyAll();
                        }
                        synchronized (buzonRevision) {
                            buzonRevision.notifyAll();
                        }

                        return;
                    }

                    if (producto != null) {
                        producto.setEstado(EstadoProducto.REPROCESADO);
                        System.out.println("Reprocesando producto ID = " + producto.getId());
                    }
                }
            }

            /* Si no hay productos en el buzón de reproceso, generar un nuevo producto */
            if (producto == null) {
                synchronized (Productor.class) {
                    if (productosGenerados >= totalProductos) {
                        return; /* Si ya se generaron todos, el hilo termina */
                    }
                    producto = new Producto(EstadoProducto.NUEVO);
                    productosGenerados++;

                    int productosFaltantes = totalProductos - productosGenerados;
                    System.out.println("Productos generados: " + productosGenerados +
                            " | Faltantes: " + productosFaltantes);
                }
            }

            /* Almacenamos el producto en el Buzón de Revisión */
            synchronized (buzonRevision) {
                while (buzonRevision.estaLleno()) {
                    try {
                        System.out.println("Buzón de revisión lleno, esperando espacio...");
                        buzonRevision.wait(); // Espera pasiva
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                buzonRevision.agregar(producto);
            }
        }
    }
}
