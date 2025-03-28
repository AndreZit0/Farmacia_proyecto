package Farmacia.C;

import Conexion.ConexionBD;
import Farmacia.M.Movimiento;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que maneja las operaciones de la base de datos para los movimientos financieros.
 */
public class MovimientoDAO {

    /**
     * Agrega un nuevo movimiento financiero a la base de datos.
     *
     * @param movimiento Objeto Movimiento con los datos del movimiento a agregar.
     * @return true si el movimiento se agregó con éxito, false en caso contrario.
     */
    public boolean agregarMovimiento(Movimiento movimiento) {
        String query = "INSERT INTO movimientos_financieros (tipo, id_pedido, categoria, fecha, monto, descripcion) VALUES (?, ?, ?, ?, ?, ?)";

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

    /**
     * Actualiza un movimiento financiero existente en la base de datos.
     *
     * @param movimiento Objeto Movimiento con los datos actualizados del movimiento.
     * @return true si el movimiento se actualizó con éxito, false en caso contrario.
     */

    public void actualizar(Movimiento movimiento){
        Connection con = ConexionBD.getConnection();

        String consultaAnterior = "SELECT monto, categoria, tipo FROM movimientos_financieros WHERE idmovimientos= ?";

        try {
            PreparedStatement stmtConsultaAnterior = con.prepareStatement(consultaAnterior);
            stmtConsultaAnterior.setInt(1, movimiento.getIdMovimientos());
            ResultSet rs = stmtConsultaAnterior.executeQuery();

            if (rs.next()) {
                int montoAnterior = rs.getInt("monto");
                String metodoPagoAnterior = rs.getString("tipo");
                String tipoMovimientoAnterior = rs.getString("categoria");


                // Restar el monto del método de pago anterior
                actualizarCaja(metodoPagoAnterior, -montoAnterior,tipoMovimientoAnterior);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al recuperar datos del movimiento anterior");
            return;
        }
    }
    public void actualizarCaja(String metodo_pago, int monto, String tipo_movimiento)
    {
        Connection con = ConexionBD.getConnection();
        String consulta = "UPDATE caja SET valor = valor + ? WHERE idcaja = 1";

        try{
            PreparedStatement pst = con.prepareStatement(consulta);

            if(tipo_movimiento.equalsIgnoreCase("egreso")){
                monto = -monto;
            }

            pst.setInt(1, monto);


            int resultado = pst.executeUpdate();

            if(resultado <= 0){
                JOptionPane.showMessageDialog(null, "Error al actualizar la caja");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    public boolean actualizarMovimiento(Movimiento movimiento) {
        String query = "UPDATE movimientos_financieros SET tipo = ?, id_pedido = ?, categoria = ?, fecha = ?, monto = ?, descripcion = ? WHERE idmovimientos = ?";

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
                JOptionPane.showMessageDialog(null, "Movimiento actualizado con éxito.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el movimiento con ID: " + movimiento.getIdMovimientos());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar movimiento: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un movimiento financiero de la base de datos por su ID.
     *
     * @param idMovimiento ID del movimiento a eliminar.
     */
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
                JOptionPane.showMessageDialog(null, "Movimiento eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el movimiento con ID: " + idMovimiento);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar movimiento: " + e.getMessage());
        }
    }
}
