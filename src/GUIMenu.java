import Farmacia.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUIMenu {
    private JButton clientesButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JPanel main;
    private JButton pedidoButton;
    private JButton productosButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;

    ClientesDAO clientesDAO = new ClientesDAO();
    CajaDAO cajaDAO = new CajaDAO();


    public GUIMenu() {

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
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUIDAO movimientosGUIDAO = new MovimientosGUIDAO();
                movimientosGUIDAO.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });
    }


    public static void main(String[]args) {

        JFrame frame = new JFrame("FARMAC TECH");
        frame.setContentPane(new GUIMenu().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    public void main() {
        JFrame frame = new JFrame("Reportes");
        frame.setContentPane(this.main);
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
