/**
 * Clase que gestiona las operaciones de base de datos para los detalles de pedidos en la farmacia.
 */
package Farmacia;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class Detalle_pedidoDAO {

    private ConexionBD conexionBD = new ConexionBD();
    private HashMap<String, Integer> ProductoMap = new HashMap<>();

    /**
     * Clase interna que representa un detalle de pedido.
     */
    public static class Detalle_pedido {
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
        public Detalle_pedido(int iddetalle_pedido, int idpedidos, int idproductos, int cantidad, int subtotal, String medida) {
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

    /**
     * Agrega un detalle de pedido a la base de datos.
     *
     * @param detallePedido Objeto Detalle_pedido con la información del pedido.
     */
    public void agregar(Detalle_pedido detallePedido) {
        Connection con = conexionBD.getConnection();

        String stockQuery = "SELECT nombre, stock, stock_minimo FROM productos WHERE idproductos = ?";
        String totalPedidoQuery = "SELECT COALESCE(SUM(CASE " +
                "WHEN medida = 'unidad' THEN cantidad " +
                "WHEN medida = 'blister' THEN cantidad * 10 " +
                "WHEN medida = 'caja' THEN cantidad * 100 " +
                "ELSE 0 END), 0) AS total_pedido " +
                "FROM detalle_pedido WHERE idproductos = ? AND idpedidos = ?";
        String insertQuery = "INSERT INTO detalle_pedido (idpedidos, idproductos, medida, cantidad, subtotal) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stockStmt = con.prepareStatement(stockQuery);
            stockStmt.setInt(1, detallePedido.getIdproductos());
            ResultSet rs = stockStmt.executeQuery();

            if (rs.next()) {
                int stockActual = rs.getInt("stock");
                int stockMinimo = rs.getInt("stock_minimo");

                int cantidadReal = switch (detallePedido.getMedida().toLowerCase()) {
                    case "unidad" -> detallePedido.getCantidad();
                    case "blister" -> detallePedido.getCantidad() * 10;
                    case "caja" -> detallePedido.getCantidad() * 100;
                    default -> 0;
                };

                // Verificar el stock disponible
                PreparedStatement totalPedidoStmt = con.prepareStatement(totalPedidoQuery);
                totalPedidoStmt.setInt(1, detallePedido.getIdproductos());
                totalPedidoStmt.setInt(2, detallePedido.getIdpedidos());
                ResultSet rsTotal = totalPedidoStmt.executeQuery();

                int totalPedido = rsTotal.next() ? rsTotal.getInt("total_pedido") : 0;

                if ((totalPedido + cantidadReal) > stockActual) {
                    JOptionPane.showMessageDialog(null, "Stock insuficiente. Ya hay " + totalPedido +
                            " unidades registradas en este pedido y el stock total es de " + stockActual + ".");
                    return;
                }

                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, detallePedido.getIdpedidos());
                insertStmt.setInt(2, detallePedido.getIdproductos());
                insertStmt.setString(3, detallePedido.getMedida());
                insertStmt.setInt(4, detallePedido.getCantidad());
                insertStmt.setInt(5, detallePedido.getSubtotal());

                int resultado = insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(null, resultado > 0 ? "Pedido agregado." : "Error al agregar el pedido.");

                rsTotal.close();
                totalPedidoStmt.close();
                insertStmt.close();
            }

            rs.close();
            stockStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error en la base de datos.");
        }
    }

    /**
     * Actualiza un detalle de pedido existente en la base de datos.
     *
     * @param detallePedido Objeto Detalle_pedido con los datos actualizados.
     */
    public void actualizar(Detalle_pedido detallePedido) {
        Connection con = conexionBD.getConnection();
        String query = "UPDATE detalle_pedido SET idpedidos = ?, idproductos = ?, medida = ?, cantidad = ? WHERE iddetalle_pedido = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, detallePedido.getIdpedidos());
            pst.setInt(2, detallePedido.getIdproductos());
            pst.setString(3, detallePedido.getMedida());
            pst.setInt(4, detallePedido.getCantidad());
            pst.setInt(5, detallePedido.getIddetalle_pedido());

            int resultado = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, resultado > 0 ? "Actualizado con éxito" : "No se pudo actualizar");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el pedido.");
        }
    }

    /**
     * Elimina un detalle de pedido de la base de datos.
     *
     * @param id ID del detalle de pedido a eliminar.
     */
    public void eliminar(int id) {
        Connection con = conexionBD.getConnection();
        String query = "DELETE FROM detalle_pedido WHERE iddetalle_pedido = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);

            int resultado = pst.executeUpdate();
            JOptionPane.showMessageDialog(null, resultado > 0 ? "Eliminado con éxito" : "No se pudo eliminar");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el pedido.");
        }
    }
}
