package com.example.dooropenerv2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class doorToggle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_toggle);

        Button toggleButton = (Button)findViewById(R.id.toggleButton);
        Button shareButton = (Button)findViewById(R.id.shareButton);
        EditText titleText = (EditText)findViewById(R.id.titleText);
        ListView userRoomsListView = (ListView) findViewById(R.id.userRoomsListView);


        //want list of rooms user has access to
        ArrayList<String> user1Rooms = new ArrayList();
        user1Rooms.add("STB 770");
        user1Rooms.add("HH 316");

//        List<String> user1Rooms_list = new ArrayList<String>(Arrays.<String>asList(String.valueOf(user1Rooms)));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, user1Rooms);
        
        String currentUser = getIntent().getStringExtra("inputUsername");
        titleText.setText("Welcome, " + currentUser);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here executes on button click

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here executes on button click

            }
        });

    }
}

