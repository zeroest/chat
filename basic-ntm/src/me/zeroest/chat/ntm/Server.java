package me.zeroest.chat.ntm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    int port;
    ServerSocket server;
    Socket client;

    Vector<ServerThread> serverThreads;

    public Server(int port) {
        this.port = port;

        makeServer(port);

        serverThreads = new Vector<>();

        try{
            while(true){
                catchSocket();
                ServerThread serverThread = new ServerThread(this);
                serverThreads.add(serverThread);
                serverThread.start();
            }
        }catch (Exception e){
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void makeServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server socket ready");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void catchSocket() {
        try {
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
