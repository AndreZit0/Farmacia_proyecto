import Farmacia.V.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUIMenu {
    private JButton clientesButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JPanel main;
    private JButton pedidoButton;
    private JButton productosButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;


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
                ReportesGUI reportesGUIDAO = new ReportesGUI();
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
        clientesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                clientesButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                clientesButton.setBackground(Color.decode("#008000"));
            }
        });
        productosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                productosButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                productosButton.setBackground(Color.decode("#008000"));
            }
        });

        pedidoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pedidoButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pedidoButton.setBackground(Color.decode("#008000"));
            }
        });
        cajaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                cajaButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                cajaButton.setBackground(Color.decode("#008000"));
            }
        });
        REPORTESButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                REPORTESButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                REPORTESButton.setBackground(Color.decode("#008000"));
            }
        });
        socketsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                socketsButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                socketsButton.setBackground(Color.decode("#008000"));
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(Color.decode("#008000"));
            }
        });
    }


    public static void main(String[]args) {

        JFrame frame = new JFrame("FARMAC TECH");
        frame.setContentPane(new GUIMenu().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200, 700);
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
