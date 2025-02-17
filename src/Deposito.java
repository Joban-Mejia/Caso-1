public class Deposito {
    private int productosAprobados = 0;
    private final int meta;
    private final BuzonReproceso buzonReproceso;

    public Deposito(int meta, BuzonReproceso buzonReproceso) {
        this.meta = meta;
        this.buzonReproceso = buzonReproceso;
    }

    public synchronized void agregar(Producto producto) {
        productosAprobados++;
        System.out.println("Producto almacenado en el depósito: ID = " + producto.getId());

        if (productosAprobados >= meta) {
            System.out.println("======================================= META ALCANZADA. ENVIANDO EL PRODUCTO FIN... =======================================");
            Producto fin = new Producto(EstadoProducto.FIN);
            buzonReproceso.agregar(fin);
            Main.finalizado = true;
        }
    }
}
