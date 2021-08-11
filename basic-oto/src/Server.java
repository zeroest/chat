import javax.swing.*;

public class Server {
    JFrame frame = new JFrame("Server");
    JTextArea textArea = new JTextArea();
    JTextField inputText = new JTextField();

    public void showClientView() {
        textArea.setEditable(false);
        textArea.setText("서버 화면 \n");
        frame.add("Center", textArea);
        frame.add("South", inputText);
        frame.setSize(300, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.showClientView();
    }
}
