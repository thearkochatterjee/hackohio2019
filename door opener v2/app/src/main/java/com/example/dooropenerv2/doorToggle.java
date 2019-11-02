package com.example.dooropenerv2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class doorToggle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_toggle);

        ImageButton osuButton = (ImageButton)findViewById(R.id.osuButton);

        osuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here executes on button click

            }
        });
    }
}
