import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    JFrame frame = new JFrame("Server");
    JTextArea textArea = new JTextArea();
    JTextField inputText = new JTextField();

    int port;
    ServerSocket server;
    Socket client;

    public void showClientView() {
        textArea.setEditable(false);
        textArea.setText("Server frame \n");
        frame.add("Center", textArea);
        frame.add("South", inputText);
        frame.setSize(300, 500);
        frame.setVisible(true);
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
        Server server = new Server();
        server.showClientView();
        server.makeServer(8081);
    }
}
