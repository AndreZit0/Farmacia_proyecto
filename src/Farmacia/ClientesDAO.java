
package Farmacia;

import Conexion.ConexionBD;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona las operaciones de base de datos para los clientes.
 * Permite obtener, agregar, actualizar y eliminar clientes en la base de datos.
 */
public class ClientesDAO {

    /**
     * Obtiene la lista de todos los clientes desde la base de datos.
     *
     * @return una lista de objetos {@link Clientes} con los datos de los clientes.
     */
    public List<Clientes> obtener_clientes() {
        List<Clientes> clientes = new ArrayList<>();
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clientes");

            while (rs.next()) {
                Clientes cliente = new Clientes(
                        rs.getInt("idclientes"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /**
     * Agrega un nuevo cliente a la base de datos.
     *
     * @param cliente Objeto {@link Clientes} con los datos del cliente a agregar.
     * @return `true` si el cliente fue agregado con éxito, `false` en caso contrario.
     */
    public boolean agregar_cliente(Clientes cliente) {
        if (cliente.getCedula().isEmpty() || cliente.getNombre().isEmpty() ||
                cliente.getTelefoono().isEmpty() || cliente.getEmail().isEmpty() ||
                cliente.getDireccion().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ningún campo puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Connection con = ConexionBD.getConnection();
        String query = "INSERT INTO clientes(cedula, nombre, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, cliente.getCedula());
            pst.setString(2, cliente.getNombre());
            pst.setString(3, cliente.getTelefoono());
            pst.setString(4, cliente.getEmail());
            pst.setString(5, cliente.getDireccion());

            int resultado = pst.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza la información de un cliente en la base de datos.
     *
     * @param id          ID del cliente a actualizar.
     * @param cedula      Nueva cédula del cliente.
     * @param nombre      Nuevo nombre del cliente.
     * @param telefono    Nuevo teléfono del cliente.
     * @param email       Nuevo correo electrónico del cliente.
     * @param direccion   Nueva dirección del cliente.
     */
    public void actualizar_cliente(int id, String cedula, String nombre, String telefono, String email, String direccion) {
        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ningún campo puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "UPDATE clientes SET cedula = ?, nombre = ?, telefono = ?, email = ?, direccion = ? WHERE idclientes = ?";
        Connection con = ConexionBD.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, cedula);
            stmt.setString(2, nombre);
            stmt.setString(3, telefono);
            stmt.setString(4, email);
            stmt.setString(5, direccion);
            stmt.setInt(6, id);

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el cliente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un cliente de la base de datos según su ID.
     *
     * @param id ID del cliente a eliminar.
     */
    public void eliminarClientes(int id) {
        Connection con = ConexionBD.getConnection();
        String query = "DELETE FROM clientes WHERE idclientes = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

