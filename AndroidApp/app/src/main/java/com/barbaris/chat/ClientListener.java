package com.barbaris.chat;

import android.widget.CursorTreeAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class ClientListener {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String address;
    private int port;
    private String login;

    private ChatActivity activity;


    public ClientListener(String address, int port, String login, ChatActivity activity) {
        this.port = port;
        this.address = address;
        this.login = login;
        this.activity = activity;

        try {
            Thread thread = new Thread(() -> {
                try {
                    this.socket = new Socket(address, port);            // TODO:  put server app to external machine
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    new MessageReader().start();
                } catch (Exception ex) {
                    ClientListener.this.downClient();
                }
            });

            thread.start();
            Thread.sleep(400);

        } catch (Exception ex) {
            System.out.println("Socket error");
            activity.finish();
        }
    }

    private void downClient() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                reader.close();
                writer.close();
            }
        } catch(Exception ignored) {}
    }


    // THREADS --------

    private class MessageReader extends Thread {
        @Override
        public void run() {
            String message;

            try {
                while(true) {
                    message = reader.readLine();

                    if(message.equals("ClientDownCode")) {
                        ClientListener.this.downClient();
                        break;
                    }

                    activity.getMessage(message);
                }
            } catch (Exception ex) {
                ClientListener.this.downClient();
            }
        }
    }

    public void writeMessage(String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        try {
            if(message.equals("ClientDownCode")) {
                writer.write("ClientDownCode" + "'\n");
                ClientListener.this.downClient();
            } else {
                writer.write(String.format("(%s) %s: %s", time, login, message));
            }

            writer.flush();
        } catch (IOException ex) {
            ClientListener.this.downClient();
        }
    }
}













