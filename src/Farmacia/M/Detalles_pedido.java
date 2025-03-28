package Farmacia.M;

import Conexion.ConexionBD;

import java.util.HashMap;
/**
 * Clase interna que representa un detalle de pedido.
 */
public class Detalles_pedido {
    private ConexionBD conexionBD = new ConexionBD();
    private HashMap<String, Integer> ProductoMap = new HashMap<>();



        private int iddetalle_pedido, idpedidos, idproductos, cantidad, subtotal;
        private String medida;

        /**
         * Constructor de la clase Detalle_pedido.
         *
         * @param iddetalle_pedido Identificador único del detalle de pedido.
         * @param idpedidos Identificador del pedido asociado.
         * @param idproductos Identificador del producto asociado.
         * @param cantidad Cantidad del producto en el pedido.
         * @param subtotal Subtotal del detalle de pedido.
         * @param medida Unidad de medida del producto (unidad, blister, caja).
         */
        public Detalles_pedido(int iddetalle_pedido, int idpedidos, int idproductos, int cantidad, int subtotal, String medida) {
            this.iddetalle_pedido = iddetalle_pedido;
            this.idpedidos = idpedidos;
            this.idproductos = idproductos;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
            this.medida = medida;
        }

        // Métodos getter y setter

    /**
     * Obtiene el identificador único del detalle del pedido.
     *
     * @return El ID del detalle del pedido.
     */
    public int getIddetalle_pedido() {
        return iddetalle_pedido;
    }

    /**
     * Establece el identificador único del detalle del pedido.
     *
     * @param iddetalle_pedido El nuevo ID para el detalle del pedido.
     */
    public void setIddetalle_pedido(int iddetalle_pedido) {
        this.iddetalle_pedido = iddetalle_pedido;
    }

    /**
     * Obtiene el identificador del pedido al que pertenece este detalle.
     *
     * @return El ID del pedido.
     */
    public int getIdpedidos() {
        return idpedidos;
    }

    /**
     * Establece el identificador del pedido al que pertenece este detalle.
     *
     * @param idpedidos El nuevo ID del pedido.
     */
    public void setIdpedidos(int idpedidos) {
        this.idpedidos = idpedidos;
    }

    /**
     * Obtiene el identificador del producto incluido en este detalle.
     *
     * @return El ID del producto.
     */
    public int getIdproductos() {
        return idproductos;
    }

    /**
     * Establece el identificador del producto incluido en este detalle.
     *
     * @param idproductos El nuevo ID del producto.
     */
    public void setIdproductos(int idproductos) {
        this.idproductos = idproductos;
    }

    /**
     * Obtiene la cantidad del producto en este detalle del pedido.
     *
     * @return La cantidad del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del producto en este detalle del pedido.
     *
     * @param cantidad La nueva cantidad del producto.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el subtotal correspondiente a este detalle del pedido.
     *
     * @return El subtotal del detalle.
     */
    public int getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal correspondiente a este detalle del pedido.
     *
     * @param subtotal El nuevo subtotal del detalle.
     */
    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Obtiene la unidad de medida del producto en este detalle del pedido.
     *
     * @return La unidad de medida (ej. "unidad", "blister", "caja").
     */
    public String getMedida() {
        return medida;
    }

    /**
     * Establece la unidad de medida del producto en este detalle del pedido.
     *
     * @param medida La nueva unidad de medida (ej. "unidad", "blister", "caja").
     */
    public void setMedida(String medida) {
        this.medida = medida;
    }
}
