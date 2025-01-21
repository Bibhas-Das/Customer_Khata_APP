package com.example.ashagrillhouse;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Button customer_list,add,export;
        add = findViewById(R.id.add);
        export = findViewById(R.id.export);
        customer_list = findViewById(R.id.customer_list);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startService(new Intent(MainActivity.this,MyService.class));

                //  When new Button clicked
                Intent add = new Intent(MainActivity.this,UploadFileActivity.class);
                startActivity(add);//indent  passing
            }
        });



        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startService(new Intent(MainActivity.this,MyService.class));

                //  When new Button clicked
                Intent export = new Intent(MainActivity.this,DownloadDatabaseActivity.class);
                startActivity(export);//indent  passing
            }
        });


        customer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startService(new Intent(MainActivity.this,MyService.class));

                //  When new Button clicked
                Intent list  = new Intent(MainActivity.this,CustomerList.class);
                startActivity(list);//indent  passing
            }
        });









        long triggerTime = System.currentTimeMillis() + (30 * 1000); // 30 seconds




    }

}