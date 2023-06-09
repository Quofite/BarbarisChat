package com.barbaris.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {
    private ClientListener client;
    public static String ip = "192.168.100.5";
    public static int port = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {}

        client = new ClientListener(ip, port, getIntent().getStringExtra("login"), ChatActivity.this);

        setContentView(R.layout.activity_chat);
    }

    public void sendMessage(View view) {
        EditText message = findViewById(R.id.messageBox);
        client.writeMessage(String.valueOf(message.getText()));
        message.setText("");
    }

    public void getMessage(String message) {
        TextView messagesBox = findViewById(R.id.messagesBox);
        messagesBox.setText(messagesBox.getText() + message + "\n");
    }
}