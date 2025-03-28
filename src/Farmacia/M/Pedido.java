package Farmacia.M;

import java.sql.Timestamp;

public class Pedido {

    int idPedidos,idclientes,total;
    String estado;
    Timestamp fecha;

    /**
     * Constructor para crear un nuevo objeto Pedido.
     *
     * @param idPedidos  El ID único del pedido.
     * @param idclientes El ID del cliente que realizó el pedido.
     * @param total      El total del pedido.
     * @param estado     El estado actual del pedido (ej. "Pendiente", "Enviado", "Entregado").
     * @param fecha      La fecha y hora en que se realizó el pedido.
     */
    public Pedido(int idPedidos, int idclientes, int total, String estado, Timestamp fecha) {
        this.idPedidos = idPedidos;
        this.idclientes = idclientes;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID del pedido.
     *
     * @return El ID del pedido.
     */
    public int getIdPedidos() {
        return idPedidos;
    }

    /**
     * Establece el ID del pedido.
     *
     * @param idPedidos El nuevo ID para el pedido.
     */
    public void setIdPedidos(int idPedidos) {
        this.idPedidos = idPedidos;
    }

    /**
     * Obtiene el ID del cliente que realizó el pedido.
     *
     * @return El ID del cliente.
     */
    public int getIdclientes() {
        return idclientes;
    }

    /**
     * Establece el ID del cliente que realizó el pedido.
     *
     * @param idclientes El nuevo ID del cliente.
     */
    public void setIdclientes(int idclientes) {
        this.idclientes = idclientes;
    }

    /**
     * Obtiene el total del pedido.
     *
     * @return El total del pedido.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Establece el total del pedido.
     *
     * @param total El nuevo total del pedido.
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Obtiene el estado del pedido.
     *
     * @return El estado del pedido (ej. "Pendiente", "Enviado", "Entregado").
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del pedido.
     *
     * @param estado El nuevo estado del pedido.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha y hora en que se realizó el pedido.
     *
     * @return La fecha y hora del pedido.
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora en que se realizó el pedido.
     *
     * @param fecha La nueva fecha y hora del pedido.
     */
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}