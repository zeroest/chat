package me.zeroest.chat.ntm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerThread extends Thread{

    ServerSocket server;
    Socket client;

    DataOutputStream outputStream;
    DataInputStream inputStream;

    Vector<ServerThread> serverThreads;

    public ServerThread(Server server) {
        this.server = server.server;
        this.client = server.client;
        this.serverThreads = server.serverThreads;

        connectInputStream();
        connectOutputStream();
    }


    @Override
    public void run() {
        try {
            while (true) {
                String msg = receiveMessageToClient();
                broadcast(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            serverThreads.remove(this);
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void connectInputStream() {
        try {
            inputStream = new DataInputStream(client.getInputStream());
            System.out.println("ServerThread.connectInputStream");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectOutputStream() {
        try {
            outputStream = new DataOutputStream(client.getOutputStream());
            System.out.println("ServerThread.connectOutputStream");
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

    public void broadcast(String message) {
        serverThreads.forEach(serverThread -> {
            serverThread.sendMessageToClient(message);
        });
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

}
