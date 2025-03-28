package Farmacia.C;

import Conexion.ConexionBD;

import java.sql.*;

/**
 * Clase que maneja las operaciones relacionadas con la caja en la base de datos.
 */
public class CajaDAO {

    private ConexionBD conexionBD = new ConexionBD();

    /**
     * Obtiene el valor actual de la caja.
     *
     * @return el valor de la caja si existe, de lo contrario retorna 0.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    public int obtenerValorCaja() throws SQLException {
        String query = "SELECT valor FROM caja LIMIT 1";
        Connection con = conexionBD.getConnection();

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("valor");
            }
        }
        return 0;
    }

    /**
     * Actualiza el valor de la caja en la base de datos.
     *
     * @param nuevoValor el nuevo valor de la caja.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException si ocurre un error en la actualización SQL.
     */
    public boolean actualizarValorCaja(int nuevoValor) throws SQLException {
        String query = "UPDATE caja SET valor = ?, formato = 'efectivo' WHERE idcaja = 1";
        Connection con = conexionBD.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, nuevoValor);
            return stmt.executeUpdate() > 0;
        }
    }
}
