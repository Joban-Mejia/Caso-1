public class Deposito {
    private int productosAlmacenados = 0;
    private final int meta;

    public Deposito(int meta) {
        this.meta = meta;
    }

    public synchronized void almacenarProducto(Producto producto) {
    productosAlmacenados++;
        if (productosAlmacenados >= meta) {
            System.out.println("Meta alcanzada. Enviando producto FIN.");
            // Enviar producto FIN al buzón de reproceso
        }
    }

    public synchronized int getProductosAlmacenados() {
        return productosAlmacenados;
    }
}