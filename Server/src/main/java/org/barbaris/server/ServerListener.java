package org.barbaris.server;

import java.io.*;
import java.net.Socket;

public class ServerListener extends Thread{
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ServerListener(Socket socket) {
        this.socket = socket;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ex) {
            System.out.println("Socket init fail");
        }
    }

    @Override
    public void run() {
        String message;

        try {
            while(true) {
                message = reader.readLine();

                if(message.equals("ClientDownCode")) {
                    this.downClient();
                    break;
                }

                for (ServerListener server: Server.serversList) {
                    server.sendMessage(message);
                }
            }
        } catch (Exception ignored) {}
    }

    private void downClient() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                reader.close();
                writer.close();

                for (ServerListener server: Server.serversList) {
                    if(server.equals(this)) {
                        server.interrupt();
                        Server.serversList.remove(this);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (Exception ignored) { }
    }


}

















