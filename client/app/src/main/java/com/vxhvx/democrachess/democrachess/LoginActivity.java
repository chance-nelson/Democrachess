package com.vxhvx.democrachess.democrachess;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    API client = new API("http://www.vxhvx.com:5000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int PERMISSION = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);

        System.out.println(PERMISSION);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION);

        System.out.println(PERMISSION);

        PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

        System.out.println(PERMISSION);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION);
        PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

        if(ActivityCompat.) System.out.println("Shits fucked, yo");

        // Declare buttons and text edit fields
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        final EditText textEditUsername = (EditText) findViewById(R.id.textInputUsername);
        final EditText textEditPassword = (EditText) findViewById(R.id.textInputPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.login(textEditUsername.getText().toString(), textEditPassword.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(client.get_jwt());
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.register(textEditUsername.getText().toString(), textEditPassword.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(client.get_jwt());
            }
        });
    }
}
