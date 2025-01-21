package com.example.ashagrillhouse;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private Context context;

    // Constructor
    public MusicPlayer(Context context){
        this.context = context;
    }

    // Method to play music
    public void playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
    }

    // Method to stop music
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
