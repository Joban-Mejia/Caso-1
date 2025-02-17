public class Producto {
    private static int contador = 0;
    private static final Object lock = new Object();
    private final int id;
    private EstadoProducto estado;

    public Producto(EstadoProducto estado) {
        synchronized (lock) {  /* evitamos condiciones de carrera al asignar ID */
            this.id = ++contador;
        }
        this.estado = estado;

        if (estado == EstadoProducto.FIN) {
            System.out.println("Producto FIN creado: ID=" + id);
        } else {
            System.out.println("Producto creado: ID=" + id + ", Estado=" + estado);
        }
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
