package Farmacia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GUIClienteSocket {
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton enviarMensajeAServidorButton;
    private JPanel main;
    private PrintWriter out;
    private BufferedReader in;

    public GUIClienteSocket() {
        conectarServidor();
        enviarMensajeAServidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();

            }
        });
    }


    public void conectarServidor() {
        try {
            String serverAddress = JOptionPane.showInputDialog("Ingrese la IP del servidor (localhost si es local)");
            if (serverAddress == null || serverAddress.isEmpty()) serverAddress = "localhost";

            Socket socket = new Socket(serverAddress, 123);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);


            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) { // acá recibo el mensaje del clienteb
                        if (receivedMessage.contains("ha salido del chat")) {
                            System.exit(0);


                        }
                        String finalReceivedMessage = receivedMessage;
                        SwingUtilities.invokeLater(() -> textArea1.append(  finalReceivedMessage + "\n")); // esta linea de code
                        // es basucmanete para ir actualizando el chat a al medida de que se van enviando mensajes
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error en el cliente: " + e.getMessage()));
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "error al conectar con el servidor: " + e.getMessage());
        }
    }

    public void enviarMensaje() {
        String sendMessage = textField1.getText();
        if (!sendMessage.isEmpty() && out != null) {

            out.println("Cliente: " + sendMessage);
            textArea1.append("Yo: " + sendMessage + "\n");

            textField1.setText("");
            if (sendMessage.equalsIgnoreCase("salir")) {
                out.println("cliente ha salido del chat");
                System.exit(0);

            }
        }
    }
    public void ejecutar () {
        JFrame frame = new JFrame("Clientes");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
