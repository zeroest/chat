import javax.swing.*;

public class Client {
    JFrame frame = new JFrame("Client");
    JTextArea textArea = new JTextArea();
    JTextField inputText = new JTextField();

    public void showClientView() {
        textArea.setEditable(false);
        textArea.setText("클라이언트 화면 \n");
        frame.add("Center", textArea);
        frame.add("South", inputText);
        frame.setSize(300, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.showClientView();
    }
}
