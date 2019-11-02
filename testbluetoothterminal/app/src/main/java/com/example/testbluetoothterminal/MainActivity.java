package com.example.testbluetoothterminal;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = "bluetooth1";

    Button btnOn, btnOff;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "98:D3:51:FD:CF:07";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnOn = (Button) findViewById(R.id.ledonbtn);
        btnOff = (Button) findViewById(R.id.ledoffbtn);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendData(1);
                Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });

        btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendData(0);
                Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
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
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
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

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private void sendData(int message) {

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(message);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
}







//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Set;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQUEST_ENABLE_BT = 0;
//    private static final int REQUEST_DISCOVER_BT = 1;
//
//    TextView mStatusBlueTv, mPairedTv;
//    ImageView mBlueIv;
//    Button mOnBtn, mOffBtn, mConnectBtn, mPairedBtn, mLEDon, mLEDoff;
//
//    BluetoothAdapter mBlueAdapter;
//    BluetoothSocket btsocket = null;
//    BluetoothDevice btdevice = null;
//    BluetoothConnector bluetoothConnector;
//
//    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    String address = null, name = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
//        mPairedTv     = findViewById(R.id.pairedTv);
//        mBlueIv       = findViewById(R.id.bluetoothIv);
//        mOnBtn        = findViewById(R.id.onbtn);
//        mOffBtn       = findViewById(R.id.offbtn);
//        mConnectBtn  = findViewById(R.id.discoverablebtn);
//        mPairedBtn    = findViewById(R.id.pairbtn);
//        mLEDon        = findViewById(R.id.ledonbtn);
//        mLEDoff       = findViewById(R.id.ledoffbtn);
//
//        //adapter
//        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        //check if bluetooth is available or not
//        if (mBlueAdapter == null){
//            mStatusBlueTv.setText("Bluetooth is not available");
//        }
//        else {
//            mStatusBlueTv.setText("Bluetooth is available");
//        }
//
//        //set image according to bluetooth status(on/off)
//        if (mBlueAdapter.isEnabled()){
////            mBlueIv.setImageResource(R.drawable.ic_action_on);
//        }
//        else {
////            mBlueIv.setImageResource(R.drawable.ic_action_off);
//        }
//
//        try {fuck();} catch(Exception e){}
//
//        //on btn click
//        mOnBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mBlueAdapter.isEnabled()){
//                    showToast("Turning On Bluetooth...");
//                    //intent to on bluetooth
//                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(intent, REQUEST_ENABLE_BT);
//                }
//                else {
//                    showToast("Bluetooth is already on");
//                }
//            }
//        });
//        //discover bluetooth btn click
//        mConnectBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        //off btn click
//        mOffBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mBlueAdapter.isEnabled()){
//                    mBlueAdapter.disable();
//                    showToast("Turning Bluetooth Off");
////                    mBlueIv.setImageResource(R.drawable.ic_action_off);
//                }
//                else {
//                    showToast("Bluetooth is already off");
//                }
//            }
//        });
//        //get paired devices btn click
//        mPairedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mBlueAdapter.isEnabled()){
//                    mPairedTv.setText("Paired Devices");
//                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
//
//                    for (BluetoothDevice device: devices){
//                        mPairedTv.append("\nDevice: " + device.getName()+ ", " + device);
//
//                    }
//
//                }
//                else {
//                    //bluetooth is off so can't get paired devices
//                    showToast("Turn on bluetooth to get paired devices");
//                }
//            }
//        });
//
//        //turn on led
//        mLEDon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//
//                if (btAdapter.isEnabled()) {
//                    SharedPreferences prefs_btdev = getSharedPreferences("btdev", 0);
//                    String btdevaddr=prefs_btdev.getString("btdevaddr","?");
//                    mPairedTv.setText("pass one");
//                    if (btdevaddr != "?")
//                    {
//                        BluetoothDevice device = btAdapter.getRemoteDevice(btdevaddr);
//
//                        UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service
//                        //UUID SERIAL_UUID = device.getUuids()[0].getUuid(); //if you don't know the UUID of the bluetooth device service, you can get it like this from android cache
//
//                        BluetoothSocket socket = null;
//
//                        try {
//                            socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
//                        } catch (Exception e) {Log.e("","Error creating socket");}
//
//                        try {
//                            socket.connect();
//                            Log.e("","Connected");
//                        } catch (IOException e) {
//                            Log.e("",e.getMessage());
//                            try {
//                                Log.e("","trying fallback...");
//
//                                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
//                                socket.connect();
//
//                                Log.e("","Connected");
//                            }
//                            catch (Exception e2) {
//                                Log.e("", "Couldn't establish Bluetooth connection!");
//                            }
//                        }
//                    }
//                    else
//                    {
//                        Log.e("","BT device not selected");
//                    }
//                }
////                bluetoothConnector = new BluetoothConnector(btdevice,true,mBlueAdapter,null);
////                try {
////                    if (btsocket!=null) {
////                        bluetoothConnector.connect().getOutputStream().write(32);
////                    }
////                } catch (Exception e) {
////                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
////                    mPairedTv.setText(e.getMessage());
////                }
//            }
//        });
//
//        //turn off led
//        mLEDoff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (btsocket!=null) {
//                        btsocket.getOutputStream().write("97".getBytes());
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case REQUEST_ENABLE_BT:
//                if (resultCode == RESULT_OK){
//                    //bluetooth is on
////                    mBlueIv.setImageResource(R.drawable.ic_action_on);
//                    showToast("Bluetooth is on");
//                }
//                else {
//                    //user denied to turn bluetooth on
//                    showToast("could't on bluetooth");
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    //toast message function
//    private void showToast(String msg){
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    private void fuck() throws IOException {
//        bluetooth_connect_device();
//    }
//
//    private void bluetooth_connect_device() throws IOException {
//        try
//        {
//            address = mBlueAdapter.getAddress();
//            Set<BluetoothDevice> pairedDevices = mBlueAdapter.getBondedDevices();
//            if (pairedDevices.size()>0)
//            {
//                for(BluetoothDevice bt : pairedDevices)
//                {
//                    address=bt.getAddress();String name = bt.getName();
//                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
//        catch(Exception we){}
//        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
//        BluetoothDevice dispositivo = mBlueAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
//        btsocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
//        btsocket.connect();
//    }
//
//}
