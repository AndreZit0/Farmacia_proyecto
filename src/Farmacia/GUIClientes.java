package Farmacia;

import Conexion.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * Clase que representa la interfaz gráfica para la gestión de clientes en la farmacia.
 * Permite agregar, actualizar, eliminar y buscar clientes en la base de datos.
 */
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
    private JButton MOVIMIENTOSFINANCIEROSButton;
    ClientesDAO clientesDAO = new ClientesDAO();
    int filas;

    /**
     * Constructor de la clase GUIClientes.
     * Inicializa los componentes de la interfaz gráfica y configura los eventos de los botones.
     */
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

        buscarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar_cliente_cedula();
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFilas = table1.getSelectedRow();

                if (selectFilas >= 0) {
                    textField1.setText((String) table1.getValueAt(selectFilas, 0));
                    textField2.setText((String) table1.getValueAt(selectFilas, 1));
                    textField3.setText((String) table1.getValueAt(selectFilas, 2));
                    textField4.setText((String) table1.getValueAt(selectFilas, 3));
                    textField5.setText((String) table1.getValueAt(selectFilas, 4));
                    textField6.setText((String) table1.getValueAt(selectFilas, 5));

                    filas = selectFilas;
                }
            }
        });
        socketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();
                SwingUtilities.getWindowAncestor(socketsButton).dispose();



            }
        });
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUIDAO productoGUIDAO = new ProductoGUIDAO();
                productoGUIDAO.main();
                SwingUtilities.getWindowAncestor(productosButton).dispose();

            }
        });

        cajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
                SwingUtilities.getWindowAncestor(cajaButton).dispose();
            }
        });
        pedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUIDAO pedidoGUIDAO = new PedidoGUIDAO();
                pedidoGUIDAO.main();
                SwingUtilities.getWindowAncestor(pedidoButton).dispose();
            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportesGUIDAO reportesGUIDAO = new ReportesGUIDAO();
                reportesGUIDAO.main();
                SwingUtilities.getWindowAncestor(REPORTESButton).dispose();
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUIDAO movimientosGUIDAO = new MovimientosGUIDAO();
                movimientosGUIDAO.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });
    }

    /**
     * Limpia los campos de entrada en la interfaz gráfica.
     */
    public void clear(){
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
    }

    /**
     * Obtiene la lista de clientes desde la base de datos y la muestra en la tabla.
     */
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

            while (rs.next()) {
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

    /**
     * Agrega un nuevo cliente a la base de datos.
     */
    public void agregar_cliente(){
        String cedula = textField2.getText();
        String nombre = textField3.getText();
        String telefono = textField4.getText();
        String email = textField5.getText();
        String direccion = textField6.getText();

        Clientes clientes = new Clientes(0, cedula, nombre, telefono, email, direccion);

        if(clientesDAO.agregar_cliente(clientes)) {
            JOptionPane.showMessageDialog(null,"Cliente agregado con éxito!");
        } else {
            JOptionPane.showMessageDialog(null,"Error de creación.");
        }
    }

    /**
     * Actualiza la información de un cliente en la base de datos.
     */
    public void actualizar_cliente(){
        int id_cliente = Integer.parseInt(textField1.getText());
        String cedula = textField2.getText();
        String nombre = textField3.getText();
        String telefono = textField4.getText();
        String email = textField5.getText();
        String direccion = textField6.getText();
        clientesDAO.actualizar_cliente(id_cliente, cedula, nombre, telefono, email, direccion);
    }

    /**
     * Elimina un cliente de la base de datos.
     */
    public void eliminar(){
        int id = Integer.parseInt(textField1.getText());
        clientesDAO.eliminarClientes(id);
    }

    /**
     * Busca un cliente por su cédula y muestra sus datos en la tabla.
     */
    public void buscar_cliente_cedula() {
        String cedula = textField2.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(null,"El campo de cédula está vacío.");
            return;
        }

        String query = "SELECT * FROM clientes WHERE cedula = ?";
        Connection con = ConexionBD.getConnection();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = new DefaultTableModel();
                modelo.addColumn("id_Clientes");
                modelo.addColumn("Cedula");
                modelo.addColumn("Nombre");
                modelo.addColumn("Telefono");
                modelo.addColumn("Email");
                modelo.addColumn("Dirección");

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
                    table1.setModel(modelo);
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente con cédula " + cedula + " no encontrado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
