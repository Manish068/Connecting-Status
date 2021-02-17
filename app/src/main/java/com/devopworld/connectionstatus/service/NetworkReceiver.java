package com.devopworld.connectionstatus.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.devopworld.connectionstatus.R;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("status", "status", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Toast.makeText(context,"Online",Toast.LENGTH_SHORT).show();
            getNotification("Online",context);
        } else {
            Toast.makeText(context,"Offline",Toast.LENGTH_SHORT).show();
            getNotification("Offline",context);
        }

    }

    private void getNotification (String content,Context context) {
        String message = content;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Simplified Coding")
                .setSmallIcon(R.drawable.ic_signal_wifi_full)
                .setContentTitle("Network Status")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }


}
