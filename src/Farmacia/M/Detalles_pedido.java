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

        public int getIddetalle_pedido() { return iddetalle_pedido; }
        public void setIddetalle_pedido(int iddetalle_pedido) { this.iddetalle_pedido = iddetalle_pedido; }

        public int getIdpedidos() { return idpedidos; }
        public void setIdpedidos(int idpedidos) { this.idpedidos = idpedidos; }

        public int getIdproductos() { return idproductos; }
        public void setIdproductos(int idproductos) { this.idproductos = idproductos; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public int getSubtotal() { return subtotal; }
        public void setSubtotal(int subtotal) { this.subtotal = subtotal; }

        public String getMedida() { return medida; }
        public void setMedida(String medida) { this.medida = medida; }
}
