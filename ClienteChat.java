public class ClienteChat {
    public static String serverAdress = "";
    public static String sendMessage = "";
    public void clienteConexion(){

        System.setProperty("apple.out.UIElement","true");
        serverAdress = JOptionPane.showInputDialog("Ingresa la IP de servidor (local hot si es local)");
        if ((serverAdress == null ||serverAdress.isEmpty()))serverAdress = "localhots";

        try (Socket socket = new Socket(serverAdress, 12345)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

            String  receivedMessage;


            do {
                sendMessage = JOptionPane.showInputDialog("Escribe tu mensaje");
                if (sendMessage == null || sendMessage.equalsIgnoreCase("salir")){
                    out.println("salir");
                    break;
                }
                out.println(sendMessage);

                receivedMessage = in.readLine();
                if (receivedMessage == null || receivedMessage.equalsIgnoreCase("salir")){
                    JOptionPane.showMessageDialog(null,"El srvidor a cerrado la conexion.");
                    break;
                }
                JOptionPane.showMessageDialog(null,"servidor dice: "+receivedMessage);
            }while (true);
        }catch (IOException e){
            JOptionPane.showMessageDialog(null,"Error en el cliente:"+e.getMessage());
        }
    }
}
