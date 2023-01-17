package me.zeroest.chat.selector;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Scanner;

public class Client {

    static Selector selector = null;
    private SocketChannel socketChannel = null;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startServer();
    }

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
        eventHandler();
    }

    public void eventHandler() {
        inputText.addActionListener(actionEvent -> {
            String sendMsg = inputText.getText();

            sendMessageToServer(sendMsg);
            inputText.setText("");
            if ("bye".equalsIgnoreCase(sendMsg)) {
                try {
                    socketChannel.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            textArea.append("Client : " + sendMsg + "\n");
            System.out.println("Client.eventHandler");
        });
    }

    public void startServer() throws IOException {
        initServer();
        Receive receive = new Receive(selector, textArea);
        new Thread(receive).start();
        showClientView();
//        startWriter();
    }

    private void initServer() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void startWriter() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.next();
                byteBuffer.clear();
                byteBuffer.put(message.getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            clearBuffer(byteBuffer);
        }
    }

    private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

    private void sendMessageToServer(String message) {
        try {
            byteBuffer.clear();
            byteBuffer.put(message.getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            clearBuffer(byteBuffer);
        }
    }

    static void clearBuffer(ByteBuffer buffer) {
        if (buffer != null) {
            buffer.clear();
        }
    }
}

class Receive implements Runnable {
    public Receive(Selector selector, JTextArea textArea) {
        this.selector = selector;
        this.textArea = textArea;
    }

    private final JTextArea textArea;
    private final Selector selector;
    private CharsetDecoder decoder = null;

    public void run() {
        Charset charset = Charset.forName("UTF-8");
        decoder = charset.newDecoder();
        try {
            while (true) {
                selector.select();
                Iterator iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();

                    if (key.isReadable())
                        read(key);

                    iterator.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        try {
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            String data = decoder.decode(byteBuffer).toString();
            System.out.println("Receive Message - " + data);
            Client.clearBuffer(byteBuffer);
            textArea.append(data + "\n");
        } catch (IOException ex) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
