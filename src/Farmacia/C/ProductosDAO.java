package Farmacia.C;

import Conexion.ConexionBD;
import Farmacia.M.Productos;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductosDAO {
    /**
     * Agrega un nuevo producto a la base de datos.
     *
     * @param productos Objeto de la clase Productos con la información a insertar.
     */ ConexionBD conexionBD = new ConexionBD();
    public void agregar(Productos productos) {

        Connection con = conexionBD.getConnection();

        String query = "INSERT INTO productos (nombre,precio,stock,stock_minimo,fecha_vencimiento,descripcion,categoria) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, productos.getNombre());
            pst.setInt(2, productos.getPrecio());
            pst.setInt(3, productos.getStock());
            pst.setInt(4, productos.getStock_minimo());
            pst.setDate(5, productos.getFechaV());
            pst.setString(6, productos.getDescripcion());
            pst.setString(7, productos.getCategoria());

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

    /**
     * Elimina un producto de la base de datos según su ID.
     *
     * @param id Identificador único del producto a eliminar.
     */
    public void eliminar(int id) {
        Connection con = conexionBD.getConnection();

        String query = "DELETE FROM productos WHERE idproductos = ?";

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

    /**
     * Actualiza los datos de un producto en la base de datos.
     *
     * @param productos Objeto de la clase Productos con los nuevos datos.
     */
    public void actualizar(Productos productos) {
        Connection con = conexionBD.getConnection();
        String query = "UPDATE productos SET nombre = ?,precio = ?, stock = ?, stock_minimo = ?, fecha_vencimiento = ?, descripcion = ?, categoria = ? WHERE idproductos = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, productos.getNombre());
            pst.setInt(2, productos.getPrecio());
            pst.setInt(3, productos.getStock());
            pst.setInt(4, productos.getStock_minimo());
            pst.setDate(5, productos.getFechaV());
            pst.setString(6, productos.getDescripcion());
            pst.setString(7, productos.getCategoria());
            pst.setInt(8, productos.getIdproductos());

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
}