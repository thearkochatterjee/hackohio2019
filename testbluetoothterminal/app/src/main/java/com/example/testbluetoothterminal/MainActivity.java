package com.example.testbluetoothterminal;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewId(R.id.pairbtn);
        mBlueIv = findViewId(R.id.bluetoothbtn);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewId(R.id.offbtn);

    }
}
