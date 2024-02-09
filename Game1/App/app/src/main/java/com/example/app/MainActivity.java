package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String APP_PREFERENCES = "app_settings";
    public static final String APP_PREFERENCES_MUSIC = "music";
    public static  final String APP_PREFERENCES_SOUND = "sound";

    private MediaPlayer player;
    private MediaPlayer buttonSound;
    private boolean playerEnabled = false; // change this to true to set music on by default
    private boolean soundEnabled = false;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSound = MediaPlayer.create(this,R.raw.buttonsound);
        player = MediaPlayer.create(this, R.raw.song);
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        // set event listeners for buttons in MainActivity
        findViewById(R.id.play_button).setOnClickListener(view -> {
            startActivity(new Intent(this, PlayActivity.class));
            playButtonSound();
        });
        findViewById(R.id.settings_button).setOnClickListener(view -> {
            new SettingsDialog().show(getSupportFragmentManager(), SettingsDialog.TAG);
            playButtonSound();
        });

        // restart playing after completion to loop it
        player.setOnCompletionListener(player -> {
            player.start();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(APP_PREFERENCES_MUSIC, playerEnabled);
        editor.putBoolean(APP_PREFERENCES_SOUND, soundEnabled);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (settings.contains(APP_PREFERENCES_MUSIC)) {
            // get setting from SharedPreferences object
            playerEnabled = settings.getBoolean(APP_PREFERENCES_MUSIC, playerEnabled);
            if (playerEnabled) {
                player.start();
            }
        }
        if (settings.contains(APP_PREFERENCES_SOUND)) {
            soundEnabled = settings.getBoolean(APP_PREFERENCES_SOUND, soundEnabled);
        }
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public boolean isPlayerEnabled() {
        return playerEnabled;
    }

    public void setPlayerEnabled(boolean isEnabled) {
        playerEnabled = isEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean isEnabled) {
        soundEnabled = isEnabled;
    }

    public void playButtonSound() {
        if (buttonSound != null && soundEnabled)
            buttonSound.start();
    }

}
