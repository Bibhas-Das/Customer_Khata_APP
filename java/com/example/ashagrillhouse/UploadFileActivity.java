package com.example.ashagrillhouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UploadFileActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 100;
    private Uri selectedFileUri;
    private TextView uploadStatus;
    private Button uploadFileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        uploadStatus = findViewById(R.id.uploadStatus);
        Button selectFileButton = findViewById(R.id.selectFileButton);
        uploadFileButton = findViewById(R.id.uploadFileButton);

        selectFileButton.setOnClickListener(v -> openFileSelector());
        uploadFileButton.setOnClickListener(v -> uploadFile());
    }

    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Allow all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                uploadStatus.setText("File Selected: " + selectedFileUri.getLastPathSegment());
                uploadFileButton.setVisibility(Button.VISIBLE);
            }
        } else {
            Toast.makeText(this, "File selection cancelled.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile() {
        if (selectedFileUri == null) {
            Toast.makeText(this, "No file selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Read content from the selected file
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();

            // Overwrite or create the database.json file
            File outputFile = new File(getFilesDir(), "database.json");
            FileOutputStream fos = new FileOutputStream(outputFile, false); // Overwrite or create
            fos.write(fileContent.toString().getBytes());
            fos.close();

            // Notify user of success
            uploadStatus.setText("File uploaded and saved as database.json.");
            Toast.makeText(this, "File uploaded successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error uploading file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
