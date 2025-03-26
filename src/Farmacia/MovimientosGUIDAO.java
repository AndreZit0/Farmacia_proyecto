package Farmacia;

import Conexion.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovimientosGUIDAO {
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
    private  CajaDAO cajaDAO = new CajaDAO();
    int filas = 0;

    public class Movimiento {
        int idMovimientos;
        String tipo;
        int idPedido;
        String categoria;
        Timestamp fecha;
        int monto;
        String descripcion;

        public Movimiento(int idMovimientos, String tipo, int idPedido, String categoria, Timestamp fecha, int monto, String descripcion) {
            this.idMovimientos = idMovimientos;
            this.tipo = tipo;
            this.idPedido = idPedido;
            this.categoria = categoria;
            this.fecha = fecha;
            this.monto = monto;
            this.descripcion = descripcion;
        }

        public int getIdMovimientos() { return idMovimientos; }
        public void setIdMovimientos(int idMovimientos) { this.idMovimientos = idMovimientos; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public int getIdPedido() { return idPedido; }
        public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public Timestamp getFecha() { return fecha; }
        public void setFecha(Timestamp fecha) { this.fecha = fecha; }

        public int getMonto() { return monto; }
        public void setMonto(int monto) { this.monto = monto; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
    public class MovimientoDAO {

        public boolean agregarMovimiento(Movimiento movimiento) {
            String query = "INSERT INTO movimientos_financieros (tipo, id_Pedido, categoria, fecha, monto, descripcion) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection con = ConexionBD.getConnection();
                 PreparedStatement pst = con.prepareStatement(query)) {

                if (con == null) {
                    JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                    return false;
                }

                pst.setString(1, movimiento.getTipo());
                pst.setInt(2, movimiento.getIdPedido());
                pst.setString(3, movimiento.getCategoria());
                pst.setTimestamp(4, movimiento.getFecha());
                pst.setDouble(5, movimiento.getMonto());
                pst.setString(6, movimiento.getDescripcion());

                int resultado = pst.executeUpdate();
                return resultado > 0;

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al agregar movimiento: " + e.getMessage());
                return false;
            }
        }



        public boolean actualizarMovimiento(Movimiento movimiento) {
            String query = "UPDATE movimientos_financieros SET tipo = ?, idPedidos = ?, categoria = ?, fecha = ?, monto = ?, descripcion = ? WHERE idmovimientos = ?";

            try (Connection con = ConexionBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {

                if (con == null) {
                    JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                    return false;
                }

                stmt.setString(1, movimiento.getTipo());
                stmt.setInt(2, movimiento.getIdPedido());
                stmt.setString(3, movimiento.getCategoria());
                stmt.setTimestamp(4, movimiento.getFecha());
                stmt.setDouble(5, movimiento.getMonto());
                stmt.setString(6, movimiento.getDescripcion());
                stmt.setInt(7, movimiento.getIdMovimientos());

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Movimiento actualizado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el movimiento con ID: " + movimiento.getIdMovimientos());
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar movimiento: " + e.getMessage());
            }
            return false;
        }

        public void eliminarMovimiento(int idMovimiento) {
            String query = "DELETE FROM movimientos_financieros WHERE idmovimientos = ?";

            try (Connection con = ConexionBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {

                if (con == null) {
                    JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                    return;
                }

                stmt.setInt(1, idMovimiento);
                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Movimiento eliminado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el movimiento con ID: " + idMovimiento);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar movimiento: " + e.getMessage());
            }
        }


    }




    private void actualizarCaja(int monto, String tipo) {
        if (tipo.equalsIgnoreCase("egreso")) {
            try {

                int valorActual = cajaDAO.obtenerValorCaja();


                int nuevoValor = valorActual - monto; // se resta el monto

                // Verificar que el valor no sea negativo o igual a 0
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
        }
    }


    public MovimientosGUIDAO() {
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
                    if (movimientoDAO.actualizarMovimiento(movimiento)){


                        obtenerDatosMovimientos();
                        clear();}
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese valores numéricos válidos en los campos ID Pedido y Monto.");
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
                PedidoGUIDAO pedidoGUIDAO = new PedidoGUIDAO();
                pedidoGUIDAO.main();
                SwingUtilities.getWindowAncestor(pedidoButton).dispose();
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
    public void ejecutar() {
        JFrame frame = new JFrame("Movimientos financeros");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
