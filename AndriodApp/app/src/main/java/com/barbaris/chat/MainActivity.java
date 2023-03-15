package com.barbaris.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.barbaris.chat.models.APIDataModel;
import com.barbaris.chat.models.UserModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText loginBox = findViewById(R.id.loginBox);
        String login = loginBox.getText().toString();

        EditText passBox = findViewById(R.id.passBox);
        String pass = passBox.getText().toString();

        Thread thread = new Thread(() -> {
            try {
                URL apiHost = new URL(APIDataModel.getHost() + "/register");
                HttpURLConnection connection = (HttpURLConnection) apiHost.openConnection();

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
                System.out.println(reader.readLine());

                reader.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        thread.start();
    }
}












