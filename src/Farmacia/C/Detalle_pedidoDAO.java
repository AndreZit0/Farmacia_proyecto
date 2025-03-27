/**
 * Clase que gestiona las operaciones de base de datos para los detalles de pedidos en la farmacia.
 */
package Farmacia.C;

import Conexion.ConexionBD;
import Farmacia.M.Detalles_pedido;

import javax.swing.*;
import java.sql.*;

public class Detalle_pedidoDAO {
    ConexionBD conexionBD = new ConexionBD();
    /**
     * Agrega un detalle de pedido a la base de datos.
     *
     * @param detallePedido Objeto Detalle_pedido con la información del pedido.
     */
    public void agregar(Detalles_pedido detallePedido) {
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
    public void actualizar(Detalles_pedido detallePedido) {
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
