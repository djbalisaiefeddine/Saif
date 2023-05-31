package com.almadina.saif;


import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    final  String TAG = "Main_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect();
        // Initialize WebView
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        // Check if the intent has extras
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("com.almadina.saif.CONNECT_WIFI")) {
            // Extract SSID and password from intent extras
            String ssid = intent.getStringExtra("ssid");
            String password = intent.getStringExtra("password");
            Log.d(TAG,"wifi crd from main activity  "+ssid+"    "+password);

            // Handle the received SSID and password
            if (ssid != null && password != null) {
                // Load the URL in the WebView
                String url = "https://google.com";  // Replace with your desired URL
                webView.loadUrl(url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Go back in WebView history if possible
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }



    void scan_and_connect(){

        WifiUtils.withContext(getApplicationContext()).enableWifi();

        WifiUtils.withContext(getApplicationContext()).scanWifi(this::getScanResults).start();



    }
    private void getScanResults(@NonNull final List<ScanResult> results)
    {
        if (results.isEmpty())
        {
            Log.i(TAG, "SCAN RESULTS IT'S EMPTY");
            return;
        }
        Log.i(TAG, "GOT SCAN RESULTS " + results);
    }


    void connect(){
        WifiUtils.withContext(getApplicationContext())
                .connectWith("Sunrise_WiFi_DD3C31_5GHz", "TesT*87654321")
                .setTimeout(40000)
                .onConnectionResult(new ConnectionSuccessListener() {
                    @Override
                    public void success() {
                        Toast.makeText(MainActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failed(@NonNull ConnectionErrorCode errorCode) {
                        Toast.makeText(MainActivity.this, "EPIC FAIL!" + errorCode.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }
}
