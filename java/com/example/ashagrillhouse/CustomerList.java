package com.example.ashagrillhouse;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomerList extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        // Initialize WebView
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        // Enable JavaScript interface for interaction
        webView.addJavascriptInterface(new WebAppInterface(), "Android");


        WebAppInterface newobj = new WebAppInterface();
        // Load local HTML file
        //runOnUiThread(() -> Toast.makeText(CustomerList.this, newobj.loadData(), Toast.LENGTH_LONG).show());
        webView.loadUrl("file:///android_asset/index.html");
    }

    public class WebAppInterface {
        @android.webkit.JavascriptInterface
        public void saveData(String data) {
            try {
                // Save text data to a file
                File file = new File(getFilesDir(), "database.json");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data.getBytes());
                fos.close();

                // Show success Toast message
                runOnUiThread(() -> Toast.makeText(CustomerList.this, "Data saved successfully!", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
                // Show error Toast message
                runOnUiThread(() -> Toast.makeText(CustomerList.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }

        @android.webkit.JavascriptInterface
        public String loadData() {
            File file = new File(getFilesDir(), "database.json");
            try {
                if (!file.exists()) {
                    // Create the file with default text content
                    FileOutputStream fos = new FileOutputStream(file);
                    String defaultContent = "[]";
                    fos.write(defaultContent.getBytes());
                    fos.close();
                    runOnUiThread(() -> Toast.makeText(CustomerList.this, "Default data file created!", Toast.LENGTH_SHORT).show());
                    return defaultContent;
                } else {
                    // Load text data from the file
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[(int) file.length()];
                    fis.read(buffer);
                    fis.close();
                    runOnUiThread(() -> Toast.makeText(CustomerList.this, "Data loaded successfully!", Toast.LENGTH_SHORT).show());
                    return new String(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(CustomerList.this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
            return ""; // Return an empty string in case of an error
        }
    }
}//Orginal Code