import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server(int port) {
        this.port = port;

        showClientView();

        makeServer(port);
        connectInputStream();
        connectOutputStream();
        sendMessageToClient("Hi Client");
        String receiveMessage = receiveMessageToClient();
    }

    JFrame frame = new JFrame("Server");
    JTextArea textArea = new JTextArea();
    JTextField inputText = new JTextField();

    int port;
    ServerSocket server;
    Socket client;

    DataOutputStream outputStream;
    DataInputStream inputStream;

    public void showClientView() {
        textArea.setEditable(false);
        textArea.setText("Server frame \n");
        frame.add("Center", textArea);
        frame.add("South", inputText);
        frame.setSize(300, 500);
        frame.setVisible(true);
    }

    public void connectInputStream() {
        try {
            inputStream = new DataInputStream(client.getInputStream());
            System.out.println("Server.connectInputStream");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectOutputStream() {
        try {
            outputStream = new DataOutputStream(client.getOutputStream());
            System.out.println("Server.connectOutputStream");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(String message) {
        try {
            outputStream.writeUTF(message);
            System.out.println("To client : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessageToClient() {
        try {
            String receiveMessage = inputStream.readUTF();
            System.out.println("From client : " + receiveMessage);

            return receiveMessage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void makeServer(int port) {
        try {

            server = new ServerSocket(port);
            System.out.println("Server socket ready");

            JTextArea readyText = new JTextArea();
            readyText.setEditable(false);
            readyText.setText("Server socket ready");
            frame.add("Center", readyText);

            client = server.accept();
            System.out.println("Client socket accepted");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server server = new Server(8081);
    }
}
