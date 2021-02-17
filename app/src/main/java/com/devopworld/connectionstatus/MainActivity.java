package com.devopworld.connectionstatus;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devopworld.connectionstatus.databinding.ActivityMainBinding;
import com.devopworld.connectionstatus.service.MyService;
import com.devopworld.connectionstatus.service.NetworkReceiver;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ENABLE_BT = 0;

    ActivityMainBinding binding;
    public static final String BroadCastStringForAction = "CheckInternet";
    private IntentFilter serviceIntentFilter, networkIntentFilter , bluetoothIntentFilter;
    private AnimationDrawable wifiAnimation;

    BluetoothAdapter bluetoothAdapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //NetworkReceiver
        networkIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkReceiver networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver,networkIntentFilter);

        //BluetoothReceiver
        bluetoothIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, bluetoothIntentFilter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Checking if bluetooth is available on the device or not
        if(bluetoothAdapter == null){
            Toast.makeText(this,"Bluetooth is not available",Toast.LENGTH_SHORT).show();
        }

        if(bluetoothAdapter.isEnabled()){
            binding.imageBluetoothSignal.setImageResource(R.drawable.ic_baseline_bluetooth_24);
        }else{
            binding.imageBluetoothSignal.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
        }


        //turning on bluetooth
        binding.buttonTurnOn.setOnClickListener(v -> {
            if(!bluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,REQUEST_ENABLE_BT);
            }
        });

        //turning of bluetooth
        binding.buttonTurnOff.setOnClickListener(v -> {
            if(bluetoothAdapter.isEnabled()){
                bluetoothAdapter.disable();
                binding.imageBluetoothSignal.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
            }
        });



        //changing layout animation
        serviceIntentFilter = new IntentFilter(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);


        if(isOnline(getApplicationContext())) {
            isOnline();
        }
        else {
            isOffline();
        }

    }

    @NotNull
    public boolean isOnline(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
            return true;
        else
            return false;



    }


    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)){
                if(intent.getStringExtra("ONLINE_STATUS").equals("true")) {
                    isOnline();
                } else {
                    isOffline();
                }
            }
        }
    };

    //isOnline
    public void isOnline(){
        binding.parent.setBackgroundColor(Color.WHITE);
        binding.imageWifiSignal.setBackgroundResource(R.drawable.wifi_animation_enabled);
        wifiAnimation = (AnimationDrawable) binding.imageWifiSignal.getBackground();
        wifiAnimation.start();
    }

    //isOffline

    public void isOffline(){
        binding.imageWifiSignal.setBackgroundResource(R.drawable.wifi_animation_disabled);
        wifiAnimation = (AnimationDrawable) binding.imageWifiSignal.getBackground();
        wifiAnimation.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(mBroadcastReceiver1,bluetoothIntentFilter);
        registerReceiver(myReceiver, serviceIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerReceiver(mBroadcastReceiver1,bluetoothIntentFilter);
        registerReceiver(myReceiver, serviceIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBroadcastReceiver1,bluetoothIntentFilter);
        registerReceiver(myReceiver, serviceIntentFilter);
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        binding.imageBluetoothSignal.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:

                        break;
                    case BluetoothAdapter.STATE_ON:
                        binding.imageBluetoothSignal.setImageResource(R.drawable.ic_baseline_bluetooth_24);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    binding.imageBluetoothSignal.setImageResource(R.drawable.ic_baseline_bluetooth_24);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}