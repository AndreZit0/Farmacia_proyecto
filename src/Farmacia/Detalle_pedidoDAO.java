package Farmacia;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class Detalle_pedidoDAO {

    private ConexionBD conexionBD = new ConexionBD();

    private HashMap<String, Integer> ProductoMap = new HashMap<>();


    public  static class Detalle_pedido{

        int iddetalle_pedido, idpedidos,idproductos,cantidad,subtotal;
        String medida;

        public Detalle_pedido(int iddetalle_pedido, int idpedidos, int idproductos, int cantidad, int subtotal, String medida) {
            this.iddetalle_pedido = iddetalle_pedido;
            this.idpedidos = idpedidos;
            this.idproductos = idproductos;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
            this.medida = medida;
        }

        public int getIddetalle_pedido() {
            return iddetalle_pedido;
        }

        public void setIddetalle_pedido(int iddetalle_pedido) {
            this.iddetalle_pedido = iddetalle_pedido;
        }

        public int getIdpedidos() {
            return idpedidos;
        }

        public void setIdpedidos(int idpedidos) {
            this.idpedidos = idpedidos;
        }

        public int getIdproductos() {
            return idproductos;
        }

        public void setIdproductos(int idproductos) {
            this.idproductos = idproductos;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public int getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(int subtotal) {
            this.subtotal = subtotal;
        }

        public String getMedida() {
            return medida;
        }

        public void setMedida(String medida) {
            this.medida = medida;
        }


    }

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
                String np = rs.getString("nombre");

                int cantidadReal = 0;
                switch (detallePedido.getMedida().toLowerCase()) {
                    case "unidad":
                        cantidadReal = detallePedido.getCantidad();
                        break;
                    case "blister":
                        cantidadReal = detallePedido.getCantidad() * 10;
                        break;
                    case "caja":
                        cantidadReal = detallePedido.getCantidad() * 100;
                        break;
                }

                // Consultar la cantidad total del producto en el mismo pedido
                PreparedStatement totalPedidoStmt = con.prepareStatement(totalPedidoQuery);
                totalPedidoStmt.setInt(1, detallePedido.getIdproductos());
                totalPedidoStmt.setInt(2, detallePedido.getIdpedidos());
                ResultSet rsTotal = totalPedidoStmt.executeQuery();

                int totalPedido = 0;
                if (rsTotal.next()) {
                    totalPedido = rsTotal.getInt("total_pedido");
                }

                // Verifica si la nueva cantidad supera el stock disponible
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

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Pedido agregado.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el pedido.");
                }
                // Cerrar recursos
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




    //actualizar
    public void actualizar(Detalle_pedido detallePedido) {
        Connection con = conexionBD.getConnection();
        String query = "UPDATE detalle_pedido SET idpedidos = ?,idproductos = ?,medida = ?,cantidad = ? WHERE iddetalle_pedido = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, detallePedido.getIdpedidos());
            pst.setInt(2, detallePedido.getIdproductos());
            pst.setString(3, detallePedido.getMedida());
            pst.setInt(4, detallePedido.getCantidad());
            pst.setInt(5, detallePedido.getIddetalle_pedido());

            int resultado = pst.executeUpdate();
            if (resultado > 0)
                JOptionPane.showMessageDialog(null, "Actualizado con Exito");
            else
                JOptionPane.showMessageDialog(null, "No Actualizado con Exito");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No Actualizado con Exito");
        }
    }

    //Eliminar
    public void eliminar(int id) {
        Connection con = conexionBD.getConnection();

        String query = "DELETE FROM detalle_pedido WHERE iddetalle_pedido = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);

            int resultado = pst.executeUpdate();

            if (resultado > 0)
                JOptionPane.showMessageDialog(null, "Eliminado con Exito");
            else
                JOptionPane.showMessageDialog(null, "No Eliminado con Exito");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No Eliminado con Exito");
        }
    }





//    public void actualizarStock(int idProducto, String tipo, int cantidad) {
//        int unidad = 1; // Por defecto para Unit
//
//        if (tipo.equals("Blister")) {
//            unidad = 10;
//        } else if (tipo.equals("Caja")) {
//            unidad = 20;
//        }
//
//        int cantidadTotal = cantidad * unidad;
//
//        Connection connection = conexionBD.getConnection();
//
//        try {
//
//            String checkQuery = "SELECT stock FROM productos WHERE idproductos = ?";
//            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
//            checkStmt.setInt(1, idProducto);
//            ResultSet rs = checkStmt.executeQuery();
//
//            if (rs.next()) {
//                int stock_actual = rs.getInt("stock");
//
//                if (stock_actual >= cantidadTotal) {
//
//                    String updateQuery = "UPDATE productos SET stock = stock - ? WHERE idproductos = ?";
//                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
//                    updateStmt.setInt(1, cantidadTotal);
//                    updateStmt.setInt(2, idProducto);
//                    updateStmt.executeUpdate();
//
//                    JOptionPane.showMessageDialog(null, "Stock updated correctly. " + cantidadTotal+ " units were discounted ");
//                } else {
//                    JOptionPane.showMessageDialog(null, "Not enough stock. Current Stock: " + stock_actual + ", Required: " + cantidadTotal);
//                }
//
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error al actualizar stock: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }




}


