package Farmacia.M;

import java.sql.Timestamp;


public class Movimiento {

    int idMovimientos,idPedido,monto;
    String tipo,categoria,descripcion;
    Timestamp fecha;

    /**
     * Constructor para crear un nuevo objeto Movimiento.
     *
     * @param idMovimientos El ID único del movimiento.
     * @param tipo          El tipo de movimiento (ej. "ingreso", "egreso").
     * @param idPedido      El ID del pedido asociado a este movimiento (si aplica).
     * @param categoria     La categoría del movimiento (ej. "venta", "compra", "gasto").
     * @param fecha         La fecha y hora en que ocurrió el movimiento.
     * @param monto         El monto del movimiento.
     * @param descripcion   Una descripción adicional del movimiento.
     */
    public Movimiento(int idMovimientos, String tipo, int idPedido, String categoria, Timestamp fecha, int monto, String descripcion) {
        this.idMovimientos = idMovimientos;
        this.tipo = tipo;
        this.idPedido = idPedido;
        this.categoria = categoria;
        this.fecha = fecha;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el ID del movimiento.
     *
     * @return El ID del movimiento.
     */
    public int getIdMovimientos() {
        return idMovimientos;
    }

    /**
     * Establece el ID del movimiento.
     *
     * @param idMovimientos El nuevo ID para el movimiento.
     */
    public void setIdMovimientos(int idMovimientos) {
        this.idMovimientos = idMovimientos;
    }

    /**
     * Obtiene el tipo de movimiento.
     *
     * @return El tipo de movimiento (ej. "ingreso", "egreso").
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de movimiento.
     *
     * @param tipo El nuevo tipo de movimiento.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el ID del pedido asociado al movimiento.
     *
     * @return El ID del pedido.
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el ID del pedido asociado al movimiento.
     *
     * @param idPedido El nuevo ID del pedido.
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene la categoría del movimiento.
     *
     * @return La categoría del movimiento (ej. "venta", "compra", "gasto").
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del movimiento.
     *
     * @param categoria La nueva categoría del movimiento.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene la fecha y hora del movimiento.
     *
     * @return La fecha y hora del movimiento.
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora del movimiento.
     *
     * @param fecha La nueva fecha y hora del movimiento.
     */
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el monto del movimiento.
     *
     * @return El monto del movimiento.
     */
    public int getMonto() {
        return monto;
    }

    /**
     * Establece el monto del movimiento.
     *
     * @param monto El nuevo monto del movimiento.
     */
    public void setMonto(int monto) {
        this.monto = monto;
    }

    /**
     * Obtiene la descripción del movimiento.
     *
     * @return La descripción del movimiento.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del movimiento.
     *
     * @param descripcion La nueva descripción del movimiento.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
