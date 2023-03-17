package org.barbaris.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static final int port = 8080;
    public static LinkedList<ServerListener> serversList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                serversList.add(new ServerListener(socket));
            }
        } finally {
            server.close();
        }
    }
}
