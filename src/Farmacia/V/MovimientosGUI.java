package Farmacia.V;

import Conexion.ConexionBD;
import Farmacia.*;
import Farmacia.C.CajaDAO;
import Farmacia.C.MovimientoDAO;
import Farmacia.M.Movimiento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

public class MovimientosGUI {
    private JPanel main;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JComboBox comboBox1;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton actualizarButton;
    private JComboBox comboBox2;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private ConexionBD conexionBD = new ConexionBD();
    private MovimientoDAO movimientoDAO = new MovimientoDAO();
    private CajaDAO cajaDAO = new CajaDAO();
    int filas = 0;

    public MovimientosGUI() {
        obtenerDatosMovimientos();
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String tipo = comboBox1.getSelectedItem().toString();
                    int idPedido = Integer.parseInt(textField3.getText().trim());
                    String categoria = comboBox2.getSelectedItem().toString();
                    int monto = Integer.parseInt(textField5.getText().trim());
                    String descripcion = textField7.getText().trim();

                    Movimiento movimiento = new Movimiento(0, tipo, idPedido, categoria, new Timestamp(System.currentTimeMillis()), monto, descripcion);


                    if(tipo.equalsIgnoreCase("ingreso")){
                        int valor_actual = cajaDAO.obtenerValorCaja();
                        int nuevoValor = valor_actual +monto;
                        cajaDAO.actualizarValorCaja(nuevoValor);

                    }


                    if (movimientoDAO.agregarMovimiento(movimiento)) {
                        actualizarCaja(monto, tipo);
                        JOptionPane.showMessageDialog(null, "Movimiento agregado exitosamente.");
                        obtenerDatosMovimientos();
                        clear();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese valores numéricos válidos en los campos ID Pedido y Monto.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(textField1.getText());
                    String tipo = comboBox1.getSelectedItem().toString();
                    int idPedido = Integer.parseInt(textField3.getText());
                    String categoria = comboBox2.getSelectedItem().toString();

                    int monto = Integer.parseInt(textField5.getText());
                    String descripcion = textField7.getText();

                    Movimiento movimiento = new Movimiento(id, tipo, idPedido, categoria, new Timestamp(System.currentTimeMillis()), monto, descripcion);
                    obtenerDatosMovimientos();
                    clear();
                    if(tipo.equalsIgnoreCase("egreso")){
                        int valor_actual = cajaDAO.obtenerValorCaja();
                        int nuevoValor = valor_actual -monto;
                        cajaDAO.actualizarValorCaja(nuevoValor);

                    }
                    if(tipo.equalsIgnoreCase("ingreso")){
                        int valor_actual = cajaDAO.obtenerValorCaja();
                        int nuevoValor = valor_actual +monto;
                        cajaDAO.actualizarValorCaja(nuevoValor);

                    }
                    if (movimientoDAO.actualizarMovimiento(movimiento)) {
                        actualizarCaja(monto, tipo);
                        JOptionPane.showMessageDialog(null, "Movimiento agregado exitosamente.");
                        obtenerDatosMovimientos();
                        clear();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese valores numéricos válidos en los campos ID Pedido y Monto.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                movimientoDAO.eliminarMovimiento(id);
                obtenerDatosMovimientos();
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
                    comboBox1.setSelectedItem(table1.getValueAt(seleccionarFlas, 1));
                    textField3.setText((String)table1.getValueAt(seleccionarFlas, 2));
                    comboBox2.setSelectedItem(table1.getValueAt(seleccionarFlas, 3));
                    textField5.setText((String)table1.getValueAt(seleccionarFlas, 4));
                    textField6.setText((String)table1.getValueAt(seleccionarFlas, 5));
                    textField7.setText((String)table1.getValueAt(seleccionarFlas, 6));



                    filas = seleccionarFlas;
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
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();
                SwingUtilities.getWindowAncestor(productosButton).dispose();

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
    }
    public void clear(){
        textField1.setText("");
        textField3.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");

    }
    public void obtenerDatosMovimientos() {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Tipo");
        modelo.addColumn("ID pedido");
        modelo.addColumn("Formato");
        modelo.addColumn("Monto");
        modelo.addColumn("Fecha");
        modelo.addColumn("Descripcion");

        table1.setModel(modelo);

        String[] dato = new String[7];
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * FROM movimientos_financieros");

            while (rs.next())
            {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);
                dato[6] = rs.getString(7);

                modelo.addRow(dato);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void actualizarCaja(int monto, String tipo) {
        if (tipo.equalsIgnoreCase("egreso")) {
            try {
                int valorActual = cajaDAO.obtenerValorCaja();
                int nuevoValor = valorActual - monto;

                if (nuevoValor < 0) {
                    JOptionPane.showMessageDialog(null, "Fondos insuficientes en la caja.");
                    return;
                }
                if (cajaDAO.actualizarValorCaja(nuevoValor)) {
                    JOptionPane.showMessageDialog(null, "Se ha actualizado el valor de la caja.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la caja.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al acceder a la base de datos.");
                ex.printStackTrace();
            }
        }}
    public void ejecutar() {
        JFrame frame = new JFrame("Movimientos financeros");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200, 700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
