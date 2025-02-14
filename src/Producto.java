public class Producto {
    private static int contador = 0;
    private final int id;
    private EstadoProducto estado;

    public Producto(EstadoProducto estado) {
        this.id = ++contador;
        this.estado = estado;
        System.out.println("Producto creado: ID=" + id + ", Estado=" + estado);
    }

    public int getId() {
        return id;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }
}
