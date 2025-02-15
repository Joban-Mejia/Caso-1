public class Deposito {
    private int productosAprobados = 0;
    private final int meta;

    public Deposito(int meta) {
        this.meta = meta;
    }

    public synchronized void agregar(Producto producto) {
        productosAprobados++;
        System.out.println("Producto almacenado en el depósito: ID=" + producto.getId());

        
        if (productosAprobados >= meta) {
            System.out.println("Se ha alcanzado la meta de productos aprobados. Generando producto FIN...");
            Producto fin = new Producto(EstadoProducto.FIN);
            notifyAll();
        }
    }
}
