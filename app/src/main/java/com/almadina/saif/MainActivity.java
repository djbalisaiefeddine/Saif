package com.almadina.saif;


import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            // Handle the received SSID and password
            if (ssid != null && password != null) {
                // Load the URL in the WebView
                String url = "https://example.com";  // Replace with your desired URL
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
}
