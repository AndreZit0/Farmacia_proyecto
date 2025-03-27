package Farmacia;

import Conexion.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;


public class ProductoGUI {
    private JPanel main;
    private JButton eliminarButton1;
    private JButton agregarButton;
    private JButton actualizarButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;

    private ConexionBD conexionBD = new ConexionBD();
    ProductosDAO productoDAO = new ProductosDAO();

    int filas;


    public ProductoGUI() {
        obtenerDatos();
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField2.getText();
                int precio = Integer.parseInt(textField3.getText());
                String categoria = comboBox1.getSelectedItem().toString();
                String descripcion = textField5.getText();
                int stock = Integer.parseInt(textField6.getText());
                int stock_minimo = Integer.parseInt(textField7.getText());
                Date fechaV = Date.valueOf(textField8.getText());

                Productos productos = new Productos(0,precio,stock,stock_minimo,nombre,descripcion,categoria,fechaV);
                productoDAO.agregar(productos);
                obtenerDatos();
                clear();
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                String nombre = textField2.getText();
                int precio = Integer.parseInt(textField3.getText());
                String categoria = comboBox1.getSelectedItem().toString();
                String descripcion = textField5.getText();
                int stock = Integer.parseInt(textField6.getText());
                int stock_minimo = Integer.parseInt(textField7.getText());
                Date fechaV = Date.valueOf(textField8.getText());

                Productos productos = new Productos(id,precio,stock,stock_minimo,nombre,descripcion,categoria,fechaV);
                productoDAO.actualizar(productos);
                obtenerDatos();
                clear();
            }
        });
        eliminarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int id = Integer.parseInt(textField1.getText());
                productoDAO.eliminar(id);
                obtenerDatos();
                clear();
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
                    comboBox1.setSelectedItem( table1.getValueAt(selectFilas,3));
                    textField5.setText((String) table1.getValueAt(selectFilas, 4));
                    textField6.setText((String) table1.getValueAt(selectFilas, 5));
                    textField7.setText((String) table1.getValueAt(selectFilas, 6));
                    Object fechaValor = table1.getValueAt(selectFilas, 7);
                    if (fechaValor != null) {
                        textField8.setText(fechaValor.toString());
                    }

                    filas = selectFilas;
                }
            }
        });
        clientesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();
                SwingUtilities.getWindowAncestor(clientesButton).dispose();


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
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
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
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });
    }

    /**
     * el metodo clear limpia todas las celdas despues de (agregar, eleminar o actualizar un producto)
     */
    public void clear() {

        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        textField8.setText("");
    }

    /**
     * <p>el metodo obtenerDatos obtiene todos los productos de la farmacia en que se encuentran en la
     * en labase de datos y los muestra en una tabla</p>
     */
    public void obtenerDatos() {
        DefaultTableModel model = new DefaultTableModel();
        model.setRowCount(0);
        model.addColumn("id");
        model.addColumn("Nombre");
        model.addColumn("Precio");
        model.addColumn("Categoria");
        model.addColumn("Descripcion");
        model.addColumn("Stock");
        model.addColumn("Stock_minimo");
        model.addColumn("Vencimiento");

        table1.setModel(model);
        String[] dato = new String[8];

        Connection con = conexionBD.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT idproductos,nombre,precio,categoria,descripcion,stock,stock_minimo,fecha_vencimiento FROM productos");
            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);
                dato[6] = rs.getString(7);
                dato[7] = rs.getString(8);

                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void main() {
        JFrame frame = new JFrame("Producto");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
