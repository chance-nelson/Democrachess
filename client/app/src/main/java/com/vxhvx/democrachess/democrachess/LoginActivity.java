/** Class defining the Login/Register activity. Responsible for sending login/register requests
 *  to the Democrachess API, recieving a JSON Web Token, and then passing that to GameActivity
 *
 *  @author: Chance Nelson
 */

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

    API client = new API("http://www.vxhvx.com:5000");  // Init an API object with the server's
                                                            // URL

    private void switch_to_game_activity() {
        /** Switch to the GameActivity, passing the needed API information with it
         */
        Intent intent = new Intent(this, GameActivity.class);  // Declare intent
        intent.putExtra("jwt", client.get_jwt());                     // Package JWT
        intent.putExtra("username", client.get_username());           // Package username
        startActivity(intent);                                              // Start GameActivity
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

        final int[] registerWhiteOrBlackTeam = {-1};                // int value for tracking team
                                                                    // for registration (0=W, 1=B)

        buttonBlackTeam.setBackgroundColor(android.R.color.white);  // Set the background colors for
        buttonWhiteTeam.setBackgroundColor(android.R.color.white);  // the team select buttons

        // OnClickListener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            boolean success = false;

            @Override
            public void onClick(View v) {
                // Make sure that the username and password text fields actually have text entered
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

                // Send a login request to the API. Should return true if the login is successful
                try {
                    success = client.login(textEditUsername.getText().toString(), textEditPassword.getText().toString());
                } catch (Exception ignored) {}

                // If the login is unsuccessful, alert the user
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

                // If the login is successful, get everything ready and switch to GameActivity
                } else {
                    switch_to_game_activity();
                }
            }
        });

        // OnClickListener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;

                // Make sure the user has selected a team to join
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

                // Make sure the user and password fields have been filled
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

                // Send a registration request to the API, returns true on success
                try {
                    success = client.register(textEditUsername.getText().toString(), textEditPassword.getText().toString(), registerWhiteOrBlackTeam[0]);
                } catch (Exception ignored) {}

                // If there is a problem registering, alert the user
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

                // If the registration is successful, get everything ready and switch to GameActivity
                } else {
                    switch_to_game_activity();
                }
            }
        });

        // OnClickListener for the black team select button
        buttonBlackTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWhiteOrBlackTeam[0] = 1;                              // Set team select
                textViewTeam.setText("Choose Team (if registering): Black");  // User feedback
            }
        });

        // OnClickListener for the white team select button
        buttonWhiteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWhiteOrBlackTeam[0] = 0;                              // Set team select
                textViewTeam.setText("Choose Team (if registering): White");  // User feedback
            }
        });
    }
}
