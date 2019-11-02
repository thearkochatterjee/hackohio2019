package com.example.dooropenerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

//login to account, account has access to different rooms with different IDs

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.door_toggle);

            EditText usernameField = (EditText)findViewById(R.id.usernameField);
            EditText passwordField = (EditText)findViewById(R.id.passwordField);
            Button loginButton = (Button)findViewById(R.id.loginButton);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Code here executes on button click

                }
            });
        }
}
