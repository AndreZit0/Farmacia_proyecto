package Farmacia;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ClientesDAO {


    //Visualizar

    public List<Clientes> obtener_clientes() {
        List<Clientes> clientes = new ArrayList<>();
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clientes");

            while (rs.next()) {
                Clientes clientes1 = new Clientes(rs.getInt("idclientes"), rs.getString("cedula"), rs.getString("nombre"), rs.getString("telefono"), rs.getString("email"), rs.getString("direccion"));


                clientes.add(clientes1);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return clientes;

    }

    public boolean agregar_cliente(Clientes clientes) {

        Connection con = ConexionBD.getConnection();
        String query = "INSERT INTO clientes( cedula, nombre, telefono,email, direccion) VALUES (?,?, ?,?,?)";


        try {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, clientes.getCedula());
            pst.setString(2, clientes.getNombre());
            pst.setString(3, clientes.getTelefoono());
            pst.setString(4, clientes.getEmail());
            pst.setString(5, clientes.getDireccion());



            int resultado = pst.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;


        }
    }

    public  void actualizar_cliente(int id, String cedula,String nombreCliente, String telefono, String email, String direccion){
        String query = "UPDATE clientes SET cedula = ?, nombre = ?, telefono = ?, email = ?, direccion = ? WHERE idclientes = ?";
        Connection con = ConexionBD.getConnection();
        try{

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, cedula);
            stmt.setString(2, nombreCliente);
            stmt.setString(3, telefono);
            stmt.setString(4, email);
            stmt.setString(5, direccion);
            stmt.setInt(6, id);

            int filas = stmt.executeUpdate();

            if (filas >0){
                JOptionPane.showMessageDialog(null,"Actualizado con exito");
            }else {
                JOptionPane.showMessageDialog(null,"No Actualizado");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void eliminarClientes(int id){
        Connection con = ConexionBD.getConnection();
        String query = "DELETE FROM clientes WHERE idclientes = ?";

        try
        {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);

            int filas = stmt.executeUpdate();

            if (filas > 0)
            {
                JOptionPane.showMessageDialog(null,"Registro eliminado exitosamente");

            }
            else
            {
                JOptionPane.showMessageDialog(null,"No se encontró");

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }







}
