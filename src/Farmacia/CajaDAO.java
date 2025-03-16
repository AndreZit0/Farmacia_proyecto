package Farmacia;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CajaDAO
{



    public List<Caja> obtener_caja() {
        List<Caja> cajas = new ArrayList<>();
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM caja");

            while (rs.next()) {
                Caja caja = new Caja(rs.getInt("idcaja"), rs.getString("formato"), rs.getInt("valor"));


                cajas.add(caja);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return cajas;

    }


//    public boolean agregar_a_caja(Caja caja) {
//
//        Connection con = ConexionBD.getConnection();
//        String query = "INSERT INTO caja( formato, valor) VALUES (?,?)";
//
//
//        try {
//            PreparedStatement pst = con.prepareStatement(query);
//
//            pst.setString(1, caja.getFormato());
//            pst.setInt(2, caja.getValor());
//
//
//
//            int resultado = pst.executeUpdate();
//            return resultado > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//
//
//        }
//    }
//
//
//    public  void actualizar_caja(int id_caja, String formato, int valor){
//        String query = "UPDATE caja SET formato = ?, valor = ? WHERE idcaja = ?";
//        Connection con = ConexionBD.getConnection();
//        try{
//
//            PreparedStatement stmt = con.prepareStatement(query);
//            stmt.setString(1, formato);
//            stmt.setInt(2, valor);
//            stmt.setInt(3, id_caja);
//
//            int filas = stmt.executeUpdate();
//
//            if (filas >0){
//                JOptionPane.showMessageDialog(null,"Actualizado con exito");
//            }else {
//                JOptionPane.showMessageDialog(null,"No Actualizado");
//            }
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void eliminar_caja(int id){
//    Connection con = ConexionBD.getConnection();
//    String query = "DELETE FROM caja WHERE idcaja = ?";
//
//    try
//    {
//        PreparedStatement stmt = con.prepareStatement(query);
//        stmt.setInt(1, id);
//
//        int filas = stmt.executeUpdate();
//
//        if (filas > 0)
//        {
//            JOptionPane.showMessageDialog(null,"Registro eliminado exitosamente");
//
//        }
//        else
//        {
//            JOptionPane.showMessageDialog(null,"No se encontró");
//
//        }
//    }
//    catch (SQLException e)
//    {
//        e.printStackTrace();
//    }
//}


}
