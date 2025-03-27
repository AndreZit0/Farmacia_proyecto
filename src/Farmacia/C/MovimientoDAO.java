package Farmacia.C;

import Conexion.ConexionBD;
import Farmacia.M.Movimiento;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovimientoDAO {

    public boolean agregarMovimiento(Movimiento movimiento) {
        String query = "INSERT INTO movimientos_financieros (tipo, idPedidos, categoria, fecha, monto, descripcion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            if (con == null) {
                JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                return false;
            }

            pst.setString(1, movimiento.getTipo());
            pst.setInt(2, movimiento.getIdPedido());
            pst.setString(3, movimiento.getCategoria());
            pst.setTimestamp(4, movimiento.getFecha());
            pst.setDouble(5, movimiento.getMonto());
            pst.setString(6, movimiento.getDescripcion());

            int resultado = pst.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar movimiento: " + e.getMessage());
            return false;
        }
    }



    public boolean actualizarMovimiento(Movimiento movimiento) {
        String query = "UPDATE movimientos_financieros SET tipo = ?, idPedidos = ?, categoria = ?, fecha = ?, monto = ?, descripcion = ? WHERE idmovimientos = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            if (con == null) {
                JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                return false;
            }

            stmt.setString(1, movimiento.getTipo());
            stmt.setInt(2, movimiento.getIdPedido());
            stmt.setString(3, movimiento.getCategoria());
            stmt.setTimestamp(4, movimiento.getFecha());
            stmt.setDouble(5, movimiento.getMonto());
            stmt.setString(6, movimiento.getDescripcion());
            stmt.setInt(7, movimiento.getIdMovimientos());

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Farmacia.M.Movimiento actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el movimiento con ID: " + movimiento.getIdMovimientos());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar movimiento: " + e.getMessage());
        }
        return false;
    }

    public void eliminarMovimiento(int idMovimiento) {
        String query = "DELETE FROM movimientos_financieros WHERE idmovimientos = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            if (con == null) {
                JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                return;
            }

            stmt.setInt(1, idMovimiento);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Farmacia.M.Movimiento eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el movimiento con ID: " + idMovimiento);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar movimiento: " + e.getMessage());
        }
    }
}
