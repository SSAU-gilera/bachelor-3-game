package com.gilera.app.anim;

import android.os.AsyncTask;
import android.widget.TextView;

import java.util.Random;

public class TextAnim extends AsyncTask<Void, String, Void> {

    private TextView textView;
    private String sentence;

    public TextAnim(TextView textView, String sentence){
        this.textView = textView;
        this.sentence = sentence;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Random random = new Random();
        for (int i = 0; i < sentence.length(); i++) {
            String charAt = String.valueOf(sentence.charAt(i));
            if (isCancelled()){
                return null;
            }
            else
                publishProgress(charAt);
            boolean plus = random.nextBoolean();
            try {
                if (plus)
                    Thread.sleep(100 + random.nextInt(50));
                else
                    Thread.sleep(100 - random.nextInt(50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        textView.setText(textView.getText() + values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        textView.setText(sentence);
    }
}
