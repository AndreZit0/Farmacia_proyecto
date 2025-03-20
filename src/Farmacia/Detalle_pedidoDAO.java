package Farmacia;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class Detalle_pedidoDAO {

    private ConexionBD conexionBD = new ConexionBD();

    private HashMap<String, Integer> ProductoMap = new HashMap<>();


    int filas = 0;

    public static class Detalle_pedido{

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

        String query = "INSERT INTO detalle_pedido (idpedidos,idproductos,medida,cantidad,subtotal) VALUES (?,?,?,?,0)";

        try {

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, detallePedido.getIdpedidos());
            pst.setInt(2, detallePedido.getIdproductos());
            pst.setString(3, detallePedido.getMedida());
            pst.setInt(4, detallePedido.getCantidad());

            int resultado = pst.executeUpdate();
            if (resultado > 0)
                JOptionPane.showMessageDialog(null, "Agregado con Exito");

            else
                JOptionPane.showMessageDialog(null, "No Agregado con Exito");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No Agregado con Exito");
        }
    }

    //actualizar
    public void actualizar(Detalle_pedido detallePedido) {
        Connection con = conexionBD.getConnection();
        String query = "UPDATE detalle_pedido SET idpedidos = ?,idproductos = ?,medidad = ?,cantidad = ? WHERE iddetalle_pedido = ?";

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


