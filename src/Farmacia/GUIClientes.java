package Farmacia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class GUIClientes {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTable table1;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JPanel main;
    private JTextField textField6;
    private JButton buscarClienteButton;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    ClientesDAO clientesDAO = new ClientesDAO();
    int filas;


    public GUIClientes() {
        obetenerClientes();
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar_cliente();
                obetenerClientes();
                clear();



            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizar_cliente();
                obetenerClientes();
                clear();

            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
                obetenerClientes();
                clear();

            }
        });



        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int seleccionarFlas = table1.getSelectedRow();

                if(seleccionarFlas >= 0){
                    textField1.setText((String)table1.getValueAt(seleccionarFlas, 0));
                    textField2.setText((String)table1.getValueAt(seleccionarFlas, 1));
                    textField3.setText((String)table1.getValueAt(seleccionarFlas, 2));
                    textField4.setText((String)table1.getValueAt(seleccionarFlas, 3));
                    textField5.setText((String)table1.getValueAt(seleccionarFlas, 4));
                    textField6.setText((String)table1.getValueAt(seleccionarFlas, 5));



                     filas = seleccionarFlas;
                }
            }
        });
        table1.addMouseListener(new MouseAdapter() {
        });
        table1.addMouseListener(new MouseAdapter() {
        });


        buscarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar_cliente_cedula();
            }
        });
    }
    public void clear(){
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");

    }


    public void obetenerClientes() {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("id_Clientes");
        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Email");
        modelo.addColumn("Dirección");

        table1.setModel(modelo);

        String[] dato = new String[6];
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * FROM clientes");

            while (rs.next())
            {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);

                modelo.addRow(dato);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public  void agregar_cliente(){


        String cedula = textField2.getText();
        String nombre = textField3.getText();
        String telefono = textField4.getText();
        String  email = textField5.getText();
        String direccion = textField6.getText();


        Clientes clientes = new Clientes(0, cedula, nombre,telefono, email,direccion);

        if(clientesDAO.agregar_cliente(clientes))
        {
            JOptionPane.showMessageDialog(null,"Cliente agregado con exito!");
        }else {
            JOptionPane.showMessageDialog(null,"Error de creacion.");
        }


    }


    public void actualizar_cliente(){


        int id_cliente = Integer.parseInt(textField1.getText());
        String cedula = textField2.getText();
        String nombre = textField3.getText();
        String telefono = textField4.getText();
        String email = textField5.getText();
        String direccion = textField6.getText();



        clientesDAO.actualizar_cliente(id_cliente,cedula,nombre,telefono,email, direccion);

    }


    public  void eliminar(){
        int id = Integer.parseInt(textField1.getText());

        clientesDAO.eliminarClientes(id);

    }

    public void buscar_cliente_cedula() {
        String cedula = textField2.getText().trim(); // encapsulo la cedula

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(null," el campo de cédula esta vacío.");
            return;
        }

        String query = "SELECT * FROM clientes WHERE cedula = ?";
        Connection con = ConexionBD.getConnection();



        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                // crear el modelo de la tabla para que se vea todo
                DefaultTableModel modelo = new DefaultTableModel();
                modelo.addColumn("id_Clientes");
                modelo.addColumn("Cedula");
                modelo.addColumn("Nombre");
                modelo.addColumn("Telefono");
                modelo.addColumn("Email");
                modelo.addColumn("Dirección");

                // Si encontramos la cedula correcta entonces que nos arroje los datos correspondientes de ese cliente
                if (rs.next()) {
                    Object[] fila = {
                            rs.getInt("idclientes"),
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            rs.getString("direccion")
                    };
                    modelo.addRow(fila);
                    table1.setModel(modelo); // mostramos resultado en la tabla



                } else {
                    JOptionPane.showMessageDialog(null, "Cliente con cedula " + cedula + " no encontrado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error exacto en la consola
            JOptionPane.showMessageDialog(null,"Error al buscar cliente: " + e.getMessage());
        }
    }
    public void ejecutar() {
        JFrame frame = new JFrame("Clientes");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
