package com.vxhvx.democrachess.democrachess;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    API client = new API("http://192.168.0.5:5000");

    private void switch_to_game_activity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("jwt", client.get_jwt());
        intent.putExtra("username", client.get_username());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Declare buttons and text edit fields
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        final EditText textEditUsername = (EditText) findViewById(R.id.textInputUsername);
        final EditText textEditPassword = (EditText) findViewById(R.id.textInputPassword);
        final ImageButton buttonWhiteTeam = (ImageButton) findViewById(R.id.imageButtonWhiteTeam);
        final ImageButton buttonBlackTeam = (ImageButton) findViewById(R.id.imageButtonBlackTeam);
        final TextView textViewTeam = (TextView) findViewById(R.id.textViewTeam);

        final int[] registerWhiteOrBlackTeam = {-1};
        buttonBlackTeam.setBackgroundColor(android.R.color.white);
        buttonWhiteTeam.setBackgroundColor(android.R.color.white);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            boolean success = false;

            @Override
            public void onClick(View v) {
                if(textEditUsername.getText().toString().equals("") || textEditPassword.getText().toString().equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("No Username/Password");
                    alertDialog.setMessage("Please type a username and password before registering");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                try {
                    success = client.login(textEditUsername.getText().toString(), textEditPassword.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(!success) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Login Error");
                    alertDialog.setMessage("Could not login. Check your username/password");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                if(success) {
                    switch_to_game_activity();
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;

                if(registerWhiteOrBlackTeam[0] == -1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("No Team Selected");
                    alertDialog.setMessage("Please select a team before registering");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                if(textEditUsername.getText().toString().equals("") || textEditPassword.getText().toString().equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("No Username/Password");
                    alertDialog.setMessage("Please type a username and password before registering");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                try {
                    success = client.register(textEditUsername.getText().toString(), textEditPassword.getText().toString(), registerWhiteOrBlackTeam[0]);
                } catch (Exception e) {

                }

                if(!success) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Registration Error");
                    alertDialog.setMessage("Could not register. Username is likely already taken");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }


            }
        });

        buttonBlackTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWhiteOrBlackTeam[0] = 0;
                textViewTeam.setText("Choose Team (if registering): Black");
            }
        });

        buttonWhiteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWhiteOrBlackTeam[0] = 1;
                textViewTeam.setText("Choose Team (if registering): White");
            }
        });
    }
}
