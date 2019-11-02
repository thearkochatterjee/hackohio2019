package com.example.dooropenerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

//login to account, account has access to different rooms with different IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_toggle);

        final EditText usernameField = (EditText)findViewById(R.id.usernameField);
        final EditText passwordField = (EditText)findViewById(R.id.passwordField);
        final TextView statusTextView = (TextView)findViewById(R.id.statusTextView);
        Button loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here executes on button click
                String inputUsername = usernameField.getText().toString();
                String inputPassword = passwordField.getText().toString();

                if (checkLogin(inputUsername, inputPassword)){
                    //login success
                    startActivity(new Intent(getApplicationContext(), doorToggle.class));
                }else{
                    //login fail
                    statusTextView.setText("Username and/or password invalid. Login failed!");
                }
            }
        });
    }

    private boolean checkLogin(String inputUsername, String inputPassword){
        boolean loginSuccess = false;
        String[] usernames = {"fong.121","again"};
        String[] passwords = {"1234","password"};

        for (int i = 0; i < usernames.length; i++){
            if (inputUsername.equals(usernames[i])&&inputPassword.equals(passwords[i])){
                loginSuccess = true;
            }
        }

        return loginSuccess;
    }
}
