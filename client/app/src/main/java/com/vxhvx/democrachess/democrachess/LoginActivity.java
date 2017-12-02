package com.vxhvx.democrachess.democrachess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    API client = new API("http://192.168.0.5:5000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Declare buttons and text edit fields
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        final EditText textEditUsername = (EditText) findViewById(R.id.textInputUsername);
        final EditText textEditPassword = (EditText) findViewById(R.id.textInputPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            boolean success;

            @Override
            public void onClick(View v) {
                try {
                    success = client.login(textEditUsername.getText().toString(), textEditPassword.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(success);

                System.out.println(client.get_jwt());
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //client.register(textEditUsername.getText().toString(), textEditPassword.getText().toString());
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
