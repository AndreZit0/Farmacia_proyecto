package Farmacia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GUICaja {
    private JTable table1;
    private JPanel main;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;


    public GUICaja() {
        obtener_valores_de_caja();



    }
    public void obtener_valores_de_caja() {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("id_Caja");
        modelo.addColumn("formato");
        modelo.addColumn("valor");

        table1.setModel(modelo);

        String[] dato = new String[3];
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * FROM caja");

            while (rs.next())
            {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);

                modelo.addRow(dato);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void ejecutar() {
        JFrame frame = new JFrame("caja");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }


}
