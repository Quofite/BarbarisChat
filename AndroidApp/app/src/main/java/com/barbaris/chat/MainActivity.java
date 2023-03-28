package com.barbaris.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.barbaris.chat.models.UserModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {}

        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText loginBox = findViewById(R.id.loginBox);
        String login = loginBox.getText().toString();

        EditText passBox = findViewById(R.id.passBox);
        String pass = passBox.getText().toString();

        try {
            URL apiHost = new URL("http://192.168.100.5:8888/login");
            HttpURLConnection connection = (HttpURLConnection) apiHost.openConnection();

            Thread thread = new Thread(() -> {
                try {
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    Gson gson = new Gson();
                    UserModel user = new UserModel();
                    user.setLogin(login);
                    user.setPassword(pass);

                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    wr.write(gson.toJson(user));
                    wr.flush();
                    wr.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    reader.close();

                    if(connection.getResponseCode() == 200) {
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra("login", login);
                        MainActivity.this.startActivity(intent);
                    } else {
                        System.out.println("An error has happened during login session: " + connection.getResponseCode());
                    }

                } catch (Exception ex) {
                    try {
                        int responseCode = connection.getResponseCode();

                        switch (responseCode) {
                            case 404:
                                System.out.println("There are no such user");
                                break;
                            case 500:
                                System.out.println("Internal server error had happened");
                                break;
                            case 400:
                                System.out.println("Bad request");
                                break;
                        }
                    } catch (IOException ignored) {}
                }
            });

            thread.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}












