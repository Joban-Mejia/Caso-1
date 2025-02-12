public class Producto {
    private EstadoProducto estado;

    public Producto(EstadoProducto estado) {
        this.estado = estado;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }
}