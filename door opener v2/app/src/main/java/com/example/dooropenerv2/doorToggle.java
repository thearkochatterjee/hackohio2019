package com.example.dooropenerv2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.UUID;


public class doorToggle extends AppCompatActivity {
    private static final String TAG = "bluetooth1";



    String fname = "C:/Users/blaar/Documents/GitHub/hackohio2019/door opener v2/app/src/main/java/com/example/dooropenerv2/Me/OwnedRooms.txt";

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "98:D3:51:FD:CF:07";

    //might need to move this to onCreate
    EditText errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent currentIntent = getIntent();
        String[] usernames = currentIntent.getStringArrayExtra("usernameArray");
        setContentView(R.layout.door_toggle);


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        Button toggleButton = (Button) findViewById(R.id.toggleButton);
        Button shareButton = (Button) findViewById(R.id.shareButton);
        EditText titleText = (EditText) findViewById(R.id.titleText);
        errorText = (EditText)findViewById(R.id.errorText);

        final ListView userRoomsListView = (ListView) findViewById(R.id.userRoomsListView);

        //want list of rooms user has access to


//        List<String> user1Rooms_list = new ArrayList<String>(Arrays.<String>asList(String.valueOf(user1Rooms)));

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user1Rooms);

        //userRoomsListView.setAdapter(arrayAdapter);


        String currentUser = currentIntent.getStringExtra("inputUsername");
        titleText.setText("Welcome, " + currentUser);

        userRoomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
            public void onClick(View v) {
                    sendData(1);
                    errorText.setText("Turn on LED");
                    //Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
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

        private BluetoothSocket createBluetoothSocket (BluetoothDevice device) throws IOException {
            if (Build.VERSION.SDK_INT >= 10) {
                try {
                    final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                    return (BluetoothSocket) m.invoke(device, MY_UUID);
                } catch (Exception e) {
                    Log.e(TAG, "Could not create Insecure RFComm Connection", e);
                }
            }
            return device.createRfcommSocketToServiceRecord(MY_UUID);
        }

        @Override
        public void onResume () {
            super.onResume();

            Log.d(TAG, "...onResume - try connect...");

            // Set up a pointer to the remote node using it's address.
            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            // Two things are needed to make a connection:
            //   A MAC address, which we got above.
            //   A Service ID or UUID.  In this case we are using the
            //     UUID for SPP.

            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e1) {
                errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
            }

            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            btAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            Log.d(TAG, "...Connecting...");
            try {
                btSocket.connect();
                Log.d(TAG, "...Connection ok...");
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                }
            }

            // Create a data stream so we can talk to server.
            Log.d(TAG, "...Create Socket...");

            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
            }
        }

        @Override
        public void onPause () {
            super.onPause();

            Log.d(TAG, "...In onPause()...");

            if (outStream != null) {
                try {

                    outStream.flush();
                } catch (IOException e) {
                    errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
                }
            }

            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
            }
        }

        private void checkBTState() {
            // Check for Bluetooth support and then check to make sure it is turned on
            // Emulator doesn't support Bluetooth and will return null
            if (btAdapter == null) {
                errorExit("Fatal Error", "Bluetooth not support");
            } else {
                if (btAdapter.isEnabled()) {
                    Log.d(TAG, "...Bluetooth ON...");
                } else {
                    //Prompt user to turn on Bluetooth
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }
            }
        }

        private void errorExit (String title, String message){
//errorText.setText(title + " - " + message);
            Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
            finish();
        }

        private void sendData (int message){

            Log.d(TAG, "...Send data: " + message + "...");

            try {
                outStream.write(message);
            } catch (IOException e) {
                String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
                if (address.equals("00:00:00:00:00:00"))
                    msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
                msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

                errorExit("Fatal Error", msg);
            }
        }
}

