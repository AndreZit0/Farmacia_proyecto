package Farmacia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GUIServidor {
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton enviarMensajeAlClienteButton;
    private JPanel main;
    private PrintWriter out;
    private BufferedReader in;
    private Socket clienteSocket;

    public GUIServidor() {
        new Thread(this::servidor).start(); // para que se conecten mutuamente
        enviarMensajeAlClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();

            }
        });
    }
    public void servidor() {
        try (ServerSocket serverSocket = new ServerSocket(123)) {

            clienteSocket = serverSocket.accept();

            in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream())); // el in
            // es lo que voy a recibir de la parte del cliente por eso se pone in.etc

            out = new PrintWriter(clienteSocket.getOutputStream(), true);
            // el out es lo que yo mando en este caso la salida, o sea la respuesta a enviar al cliente

            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) { // recibi el mensaje del cliente con el in
                if (receivedMessage.contains("ha salido del chat")) {
                    System.exit(0);


                }

                String finalReceivedMessage = receivedMessage;
                SwingUtilities.invokeLater(() -> textArea1.append( finalReceivedMessage + "\n" ));
            }

            clienteSocket.close();
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error en el servidor: " + e.getMessage()));
        }
    }

    public void enviarMensaje() {
        String sendMessage = textField1.getText();
        if (!sendMessage.isEmpty() && out != null) {
            out.println("Farmacia: "+sendMessage);
            textArea1.append( "Yo: " +sendMessage  + "\n");
            textField1.setText("");
        }

        if(sendMessage.equalsIgnoreCase("salir")){
            out.println("Servidor ha salido del chat");
            System.exit(0);

        }
    }





    public void ejecutar() {
        JFrame frame = new JFrame("Servidor");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
