package com.almadina.saif;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public class WifiConnectionService extends Service {

    private static final String TAG = "WifiConnectionService";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"service started");
        if (intent != null ) {
            // Extract SSID and password from intent extras
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey("ssid") && extras.containsKey("password")) {
                String ssid = extras.getString("ssid");
                String password = extras.getString("password");

                // Connect to Wi-Fi network and perform speed test
                connectToWifi(ssid, password);
            }
        }

        // Return START_STICKY to ensure the service is restarted if it gets killed by the system
        return START_STICKY;
    }

    private void connectToWifi(String ssid, String password) {
        Log.d(TAG,"connecting to wifi "+ssid +"   "+password);
        // Connect to Wi-Fi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Check if WiFi is enabled
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        // Create a WifiConfiguration object
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = ssid;
        wifiConfig.preSharedKey = password;

        // Add the network to the list of configured networks
        int netId = wifiManager.addNetwork(wifiConfig);

        // Connect to the network

        wifiManager.enableNetwork(netId, true);
    }

    private void onWifiConnected(String ssid) {
        // Perform the speed test here

        // Create a notification to indicate the completion of the Wi-Fi connection and speed test
        Notification notification = buildNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    private Notification buildNotification() {
        // Customize the notification according to your requirements
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Wi-Fi Connection Complete")
                .setContentText("Wi-Fi connection and speed test completed successfully.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);

        // Open the web view activity when the notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Not used, as this service does not support binding
        return null;
    }
}
