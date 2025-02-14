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
                            System.out.println("Se han generado todos los productos. Enviando FIN...");
                            Producto fin = new Producto(EstadoProducto.FIN);
                            buzonRevision.agregar(fin);
                            Main.finalizado = true; /* Marca la finalización para todos los hilos */
                            break; /* Asegura que el productor salga del bucle */

                            /*
                             * Este break dentro de synchronized (Productor.class)
                             * solo evita que ese hilo genere más productos,
                             * pero si otros hilos siguen ejecutándose,
                             * el while (!Main.finalizado) los mantiene vivos.
                             */

                        }
                        producto = new Producto(EstadoProducto.NUEVO);
                        productosGenerados++;

                        /* manda Producto FIN cuando se han generado todos los productos */
                        if (productosGenerados >= totalProductos) {
                            System.out.println("Se han generado todos los productos. Enviando FIN...");
                            Producto fin = new Producto(EstadoProducto.FIN);
                            buzonRevision.agregar(fin);
                        }
                    }
                }

                buzonRevision.agregar(producto);
            }
        }
    }
}