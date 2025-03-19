package Farmacia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class PedidoGUIDAO {

    private PedidoDAO pedidoDAO = new PedidoDAO();
    private ConexionBD conexionBD = new ConexionBD();
    private JPanel main;
    private JTable Table1;
    private JTextField textField1;
    private JTextField textField4;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton verFacButton;
    private JComboBox comboBox2;
    private JButton registarClientesButton;
    private JComboBox comboBox3;

    private HashMap<String, Integer> clienteMap = new HashMap<>();
    int filas = 0;

    public class Pedido{

        int idPedidos, idclientes, total;
        String estado;
        Timestamp fecha;

        public Pedido(int idPedidos, int idclientes, int total, String estado, Timestamp fecha) {
            this.idPedidos = idPedidos;
            this.idclientes = idclientes;
            this.total = total;
            this.estado = estado;
            this.fecha = fecha;
        }

        public int getIdPedidos() {
            return idPedidos;
        }

        public void setIdPedidos(int idPedidos) {
            this.idPedidos = idPedidos;
        }

        public int getIdclientes() {
            return idclientes;
        }

        public void setIdclientes(int idclientes) {
            this.idclientes = idclientes;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public Timestamp getFecha() {
            return fecha;
        }

        public void setFecha(Timestamp fecha) {
            this.fecha = fecha;
        }
    }

    class PedidoDAO {
        public void agregar(Pedido pedido) {
            Connection con = conexionBD.getConnection();

            String query = "INSERT INTO pedidos (idclientes,fecha,estado,total) VALUES (?,?,?,0)";

            try {

                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, pedido.getIdclientes());
                pst.setTimestamp(2, pedido.getFecha());
                pst.setString(3, pedido.getEstado());

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

        //actualizar
        public void actualizar(Pedido pedido) {
            Connection con = conexionBD.getConnection();
            String query = "UPDATE pedidos SET idclientes = ?,fecha = ?,estado = ? WHERE idPedidos = ?";

            try {
                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, pedido.getIdclientes());
                pst.setTimestamp(2, pedido.getFecha());
                pst.setString(3, pedido.getEstado());
                pst.setInt(4, pedido.getIdPedidos());

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

        //Eliminar
        public void eliminar(int id) {
            Connection con = conexionBD.getConnection();

            String query = "DELETE FROM pedidos WHERE idPedidos = ?";

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

        public int obtenerIdSeleccionado(JComboBox<String> comboBox, HashMap<String, Integer> map) {
            String seleccionado = (String) comboBox.getSelectedItem();
            return map.getOrDefault(seleccionado, -1); // Si no encuentra el nombre, devuelve -1
        }

        public void obtener_clientes(){

            String query= "Select idClientes,cedula, nombre from clientes";

            Statement st;
            ConexionBD con = new ConexionBD();

            try {
                st = con.getConnection().createStatement();
                ResultSet rs = st.executeQuery(query);
                comboBox2.removeAllItems(); // Limpiar antes de agregar nuevos elementos
                clienteMap.clear(); // También limpiar el mapa

                while(
                        rs.next()) {
                    int id = rs.getInt("idClientes");
                    String nombre = rs.getString("nombre");
                    String cedula = rs.getString("cedula");

                    String identificacion = cedula+" / "+nombre;

                    clienteMap.put(identificacion, id);
                    comboBox2.addItem(identificacion);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public PedidoGUIDAO() {
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int idCliente = pedidoDAO.obtenerIdSeleccionado(comboBox2, clienteMap);
                    Timestamp fecha = new Timestamp(System.currentTimeMillis()); // Fecha y hora actual
                    String estado = comboBox3.getSelectedItem().toString(); // Obtener el estado del JComboBox

                    Pedido pedido = new Pedido(0, idCliente, 0, estado, fecha);
                    pedidoDAO.agregar(pedido);

                    obtenerDatosPed();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar el pedido.");
                }
                obtenerDatosPed();
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                int idCliente = pedidoDAO.obtenerIdSeleccionado(comboBox2,clienteMap);
                Timestamp fecha = new Timestamp(System.currentTimeMillis());
                String estado = comboBox3.getSelectedItem().toString();

                Pedido pedido = new Pedido(id,idCliente,0,estado,fecha);
                pedidoDAO.actualizar(pedido);

                obtenerDatosPed();
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                pedidoDAO.eliminar(id);

                obtenerDatosPed();
            }
        });
        verFacButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFilas = Table1.getSelectedRow();

                if (selectFilas >= 0) {
                    textField1.setText((String) Table1.getValueAt(selectFilas,0));
                    comboBox3.setSelectedItem( Table1.getValueAt(selectFilas,3));
                    comboBox2.setSelectedItem( Table1.getValueAt(selectFilas,1));

                    filas = selectFilas;
                }
            }
        });
    }
    public void limpiar(){
        textField1.setText("");
        textField4.setText("");
    }
    public void obtenerDatosPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre de Cliente");
        model.addColumn("Fecha");
        model.addColumn("Estado");

        Table1.setModel(model);
        String[] dato = new String[4];
        Connection con = conexionBD.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.idPedidos, c.nombre, p.fecha, p.estado FROM pedidos AS p JOIN clientes AS c ON p.idclientes = c.idClientes;");

            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);

                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void main() {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
        obtenerDatosPed();
        pedidoDAO.obtener_clientes();
    }
}
