package com.example.ashagrillhouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class DownloadDatabaseActivity extends AppCompatActivity {

    private Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_database);

        downloadButton = findViewById(R.id.downloadButton); // Button to download the file

        // Button click listener to download the file
        downloadButton.setOnClickListener(v -> downloadFile());
    }

    private void downloadFile() {
        // Locate the database.json file in the internal storage
        File jsonFile = new File(getFilesDir(), "database.json");

        if (jsonFile.exists()) {
            try {
                // Use FileProvider to get a content URI for the file
                Uri fileUri = FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        jsonFile
                );

                // Create an Intent to share the file
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/json");
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission to receiving app
                startActivity(Intent.createChooser(intent, "Download Database File"));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error downloading file: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No database file found.", Toast.LENGTH_SHORT).show();
        }
    }
}
