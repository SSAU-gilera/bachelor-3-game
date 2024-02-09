package com.example.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SettingsDialog extends DialogFragment {

    public static final String TAG = "SettingsDialog";

    private ImageView closeOptions;
    private Switch switchMusic;
    private Switch switchSound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.settings_dialog, container, false);
        switchMusic = view.findViewById(R.id.switch_music);
        switchSound = view.findViewById(R.id.switch_sound);
        closeOptions = view.findViewById(R.id.closeOptionsImageView);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        switchMusic.setChecked(mainActivity.isPlayerEnabled());
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainActivity.playButtonSound();
            MediaPlayer player = mainActivity.getPlayer();
            if (player == null)
                return;
            mainActivity.setPlayerEnabled(isChecked);
            if (isChecked) {
                player.start();
            } else {
                player.pause();
            }
        });
        switchSound.setChecked(mainActivity.isSoundEnabled());
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainActivity.playButtonSound();
            mainActivity.setSoundEnabled(isChecked);
        });
        closeOptions.setOnClickListener(imageView -> {
            mainActivity.playButtonSound();
            dismiss();
        });
        return view;
    }

}