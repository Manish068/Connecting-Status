package com.devopworld.connectionstatus.service;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.devopworld.connectionstatus.R;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//bluetooth
        String action = intent.getAction();
        int state;

        switch(action)
        {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_OFF)
                {
                    getNotification("Bluetooth is off",context);
                    Log.d("BroadcastActions", "Bluetooth is off");
                }
                else if (state == BluetoothAdapter.STATE_TURNING_OFF)
                {
                    Toast.makeText(context, "Bluetooth is turning off", Toast.LENGTH_SHORT).show();
                    Log.d("BroadcastActions", "Bluetooth is turning off");
                }
                else if(state == BluetoothAdapter.STATE_ON)
                {
                    getNotification("Bluetooth is Active",context);
                    Log.d("BroadcastActions", "Bluetooth is on");
                }
                else if(state == BluetoothAdapter.STATE_TURNING_ON){
                    Toast.makeText(context, "Bluetooth is turning on", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void getNotification (String content,Context context) {
        String message = content;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Simplified Coding")
                .setSmallIcon(R.drawable.ic_round_bluetooth_24)
                .setContentTitle("Bluetooth Status")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(2,builder.build());
    }
}
