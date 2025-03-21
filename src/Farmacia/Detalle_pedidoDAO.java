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

        String stock = "SELECT stock FROM productos WHERE idproductos = ?";
        String insertQuery = "INSERT INTO detalle_pedido (idpedidos, idproductos, medida, cantidad, subtotal) VALUES (?, ?, ?, ?, 0)";

        try {
            PreparedStatement stockStmt = con.prepareStatement(stock);
            stockStmt.setInt(1, detallePedido.getIdproductos());
            ResultSet rs = stockStmt.executeQuery();

            if (rs.next()) {
                int stockActual = rs.getInt("stock");

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

                if (stockActual >= cantidadReal) {
                    PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                    insertStmt.setInt(1, detallePedido.getIdpedidos());
                    insertStmt.setInt(2, detallePedido.getIdproductos());
                    insertStmt.setString(3, detallePedido.getMedida());
                    insertStmt.setInt(4, detallePedido.getCantidad());

                    int resultado = insertStmt.executeUpdate();

                    if (resultado > 0) {
                        JOptionPane.showMessageDialog(null, "Pedido agregado.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al agregar el pedido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Stock insuficiente para este producto.");
                }
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
}


