import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
    JFrame frame = new JFrame("Client");
    JTextArea textArea = new JTextArea();
    JTextField inputText = new JTextField();

    String host;
    int port;
    Socket clientSocket;

    public void showClientView() {
        textArea.setEditable(false);
        textArea.setText("클라이언트 화면 \n");
        frame.add("Center", textArea);
        frame.add("South", inputText);
        frame.setSize(300, 500);
        frame.setVisible(true);
    }

    public void throwSocket(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Throw socket to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.showClientView();
        client.throwSocket("localhost", 8081);
    }
}
