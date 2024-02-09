package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    TextAnimation.Word word;
    TextView textView;
    boolean actionFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        textView = findViewById(R.id.text_start);
        printText(getResources().getString(R.string.string_1));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!actionFinished) {
            actionFinished = true;
            TextAnimation.endTextPrinting(word, textView);
        } else {
            actionFinished = false;
            printText(getResources().getString(R.string.string_2));
        }

        return true;
    }

    public void printText(String text) {
        textView.setText("");
        word = new TextAnimation.Word(100, text);
        word.setOffset(50);
        TextAnimation.printText(word, textView);
    }
}
