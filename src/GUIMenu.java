import Farmacia.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUIMenu {
    private JButton clientesButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JPanel main;
    private JButton pedidoButton;
    private JButton productosButton;

    ClientesDAO clientesDAO = new ClientesDAO();
    CajaDAO cajaDAO = new CajaDAO();


    public GUIMenu() {
        clientesDAO.obtener_clientes();
        cajaDAO.obtener_caja();

        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();

            }
        });
        socketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();



            }
        });

        cajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
            }
        });
        pedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUIDAO pedidoGUIDAO = new PedidoGUIDAO();
                pedidoGUIDAO.main();
            }
        });
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUIDAO productoGUIDAO = new ProductoGUIDAO();
                productoGUIDAO.main();

            }
        });
    }

    public static void main(String[]args) {

        JFrame frame = new JFrame("FARMACIA");
        frame.setContentPane(new GUIMenu().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
