/*
    Programmer name : Bibhas Das
    Date : 4 July 2024
    Page : Flush Page
 */


package com.example.ashagrillhouse;

import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class FlashPage extends AppCompatActivity {

    private static final String TAG = "FlashPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flash_page);

        //run paralalry and delay for 4 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(FlashPage.this, MainActivity.class);
                startActivity(home);//indent  passing
                finish();
            }
        }, 4000);


        /*// In another activity or file
        MusicPlayer mp = new MusicPlayer(this); // Pass the current context
        mp.playMusic(); // Call the method to play music
        //mp.stopMusic();
        */
    }

}
