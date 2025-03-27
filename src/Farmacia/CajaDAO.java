
package Farmacia;

import Conexion.ConexionBD;

import java.sql.*;

public class CajaDAO
{
    private ConexionBD conexionBD = new ConexionBD();



    public int obtenerValorCaja() throws SQLException {
        String query = "SELECT valor FROM caja LIMIT 1";
        Connection con = conexionBD.getConnection();  // Obtener conexión correctamente


        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("valor");
            }
        }
        return 0;
    }


    public boolean actualizarValorCaja(int nuevoValor) throws SQLException {
        String query = "UPDATE caja SET valor = ?, formato = 'efectivo' WHERE idcaja = 1";
        Connection con = conexionBD.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, nuevoValor);
            return stmt.executeUpdate() > 0;
        }
    }
//
//



}