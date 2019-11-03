package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DatabaseHelper mdatabasehelper;
    private Button btnadd, btnviewdata;
    private TextView txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtname = (TextView)findViewById(R.id.txtname);
        btnadd = (Button) findViewById(R.id.btnadd);
        btnviewdata = (Button) findViewById(R.id.btnviewdata);
        mdatabasehelper = new DatabaseHelper(this);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEntry = txtname.getText().toString();
                if(txtname.getText().length() != 0){
                    AddData(newEntry);
                    txtname.setText("");
                }
                else {
                    toastMessage("You must put something in the text field!");
                }
            }
        });

        btnviewdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }

    public void AddData(String newEntry){
        boolean insertData = mdatabasehelper.addData(newEntry);
        if(insertData){
            toastMessage("Data Successfully Inserted!");
        }
        else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
