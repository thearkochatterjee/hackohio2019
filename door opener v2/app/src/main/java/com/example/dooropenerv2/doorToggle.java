package com.example.dooropenerv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class doorToggle extends AppCompatActivity {

    Intent currentIntent = getIntent();
    private String[] usernames = currentIntent.getStringArrayExtra("usernameArray");

    String fname = "C:/Users/blaar/Documents/GitHub/hackohio2019/door opener v2/app/src/main/java/com/example/dooropenerv2/Me/OwnedRooms.txt";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_toggle);



        Button toggleButton = (Button)findViewById(R.id.toggleButton);
        Button shareButton = (Button)findViewById(R.id.shareButton);
        EditText titleText = (EditText)findViewById(R.id.titleText);
        final ListView userRoomsListView = (ListView) findViewById(R.id.userRoomsListView);

        //want list of rooms user has access to


//        List<String> user1Rooms_list = new ArrayList<String>(Arrays.<String>asList(String.valueOf(user1Rooms)));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user1Rooms);

        userRoomsListView.setAdapter(arrayAdapter);


        String currentUser = currentIntent.getStringExtra("inputUsername");
        titleText.setText("Welcome, " + currentUser);

        userRoomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                user1Rooms.get(position);
            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here executes on button click
                //opens and closes a door
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code here executes on button click
                //asks user for name of user, then adds the selected room to that user's "owned" rooms

            }
        });

    }
}

