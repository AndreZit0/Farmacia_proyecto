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
 * Clase ProductoGUIDAO.
 *
 * <p>Esta clase representa la interfaz gráfica para la gestión de productos.</p>
 *
 * <p>Tiene funcionalidades para agregar, actualizar y eliminar productos
 * en una base de datos, interactuando con la clase {@code ProductoDAO} para la manipulación de datos.</p>
 *
 * <p>Incluye componentes gráficos como botones, cuadros de texto, tablas y combos
 * para facilitar la interacción con el usuario.</p>
 *
 */
public class ProductoGUIDAO {
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
    private ProductoDAO productoDAO = new ProductoDAO();

    int filas;

    /**
     * Representa los productos en el inventario de la farmacia.
     *
     * <p>La clase Productos almacena información sobre un producto,
     * incluyendo su identificador, precio, stock disponible, stock mínimo,
     * nombre, descripción, categoría y la fecha de vencimiento.</p>
     *
     * <p>Proporciona métodos para acceder y modificar estos atributos.</p>
     *
     */
    class Productos {

        int idproductos,precio,	stock,stock_minimo;
        String nombre,descripcion,categoria;
        Date fechaV;

        /**
         * Constructor para crear un nuevo producto con todos sus atributos.
         *
         * @param idproductos Identificador único del producto.
         * @param precio Precio del producto.
         * @param stock Cantidad disponible en el inventario.
         * @param stock_minimo Cantidad mínima requerida en el inventario.
         * @param nombre Nombre del producto.
         * @param descripcion Breve descripción del producto.
         * @param categoria Categoría a la que pertenece el producto.
         * @param fechaV Fecha de vencimiento del producto.
         */

        public Productos(int idproductos, int precio, int stock, int stock_minimo,String nombre, String descripcion, String categoria, Date fechaV) {
            this.idproductos = idproductos;
            this.precio = precio;
            this.stock = stock;
            this.stock_minimo = stock_minimo;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.categoria = categoria;
            this.fechaV = fechaV;
        }

        /**
         * Obtiene el id del producto
         * @return Id del producto
         */
        public int getIdproductos() {
            return idproductos;
        }

        /**
         * establece el nuevo id del producto
         * @param idproductos nuevo id del producto
         */

        public void setIdproductos(int idproductos) {
            this.idproductos = idproductos;
        }

        /**
         * obtiene el precio del producto
         * @return Precio del producto
         */
        public int getPrecio() {
            return precio;
        }

        /**
         * establece el nuevo precio del producto
         * @param precio Nuevo precio del producto
         */
        public void setPrecio(int precio) {
            this.precio = precio;
        }

        /**
         * obtiene el stock del producto
         * @return Stock de producto
         */
        public int getStock() {
            return stock;
        }

        /***
         * establece el nuevo stock
         * @param stock Stock nuevo
         */
        public void setStock(int stock) {
            this.stock = stock;
        }

        /**
         * obtiene el stock minimo
         * @return Stock minimo
         */
        public int getStock_minimo() {
            return stock_minimo;
        }

        /**
         * establece el nuevo stock minimo
         * @param stock_minimo StocK            minimo
         */
        public void setStock_minimo(int stock_minimo) {
            this.stock_minimo = stock_minimo;
        }

        /**
         * obtiene el nombre
         * @return Nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * establece el nuevo nommbre
         * @param nombre Nuevo nombre
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * obtiene la descripcion del producto
         * @return Descripcion del producto
         */
        public String getDescripcion() {
            return descripcion;
        }

        /**
         * establece una nueva descripcion del producto
         * @param descripcion Nueva descripcion del producto
         */
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        /**
         * obtiene la categoria del producto
         * @return categoria del producto
         */
        public String getCategoria() {
            return categoria;
        }

        /**
         * establece la categoria nueva del producto
         * @param categoria nueva categoria del producto
         */
        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        /**
         * obtiene la fecha de vencimiento del producto
         * @return fecha de vencimiento del producto
         */
        public Date getFechaV() {
            return fechaV;
        }

        /**
         * establece nueva fecha de venciminto del producto
         * @param fechaV Nueva fecha de vencimiento del producto
         */
        public void setFechaV(Date fechaV) {
            this.fechaV = fechaV;
        }
    }

    /**
     * Clase ProductoDAO. Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
     * sobre la tabla "productos" en la base de datos.
     */
    class ProductoDAO {

        /**
         * Agrega un nuevo producto a la base de datos.
         *
         * @param productos Objeto de la clase Productos con la información a insertar.
         */
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

    /**
     * Clase ProductoGUIDAO que gestiona la interfaz gráfica de usuario (GUI)
     * para la administración de productos en el sistema.
     */
    public ProductoGUIDAO() {
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
                MovimientosGUIDAO movimientosGUIDAO = new MovimientosGUIDAO();
                movimientosGUIDAO.ejecutar();
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
