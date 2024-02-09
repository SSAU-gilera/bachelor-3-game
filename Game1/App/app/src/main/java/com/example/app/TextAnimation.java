package com.example.app;

import android.widget.TextView;

import java.util.Random;

public class TextAnimation {

    public static void printText(final Word word, final TextView textView) {
        Random random = new Random();
        int currentRandOffset = random.nextInt(word.offset);
        boolean addOrSubtract = random.nextBoolean();
        long finalDelay = addOrSubtract ?
                word.delayBetweenSymbols + currentRandOffset :
                word.delayBetweenSymbols - currentRandOffset;
        if (finalDelay < 0) finalDelay = 0;
        word.runnable = () -> {
            String charAt = String.valueOf(word.word.charAt(word.currentCharacterIndex));
            ++word.currentCharacterIndex;
            textView.setText(textView.getText() + charAt);
            if (word.currentCharacterIndex >= word.word.length()) return;
            printText(word, textView);
        };
        textView.postDelayed(word.runnable, finalDelay);
    }

    public static void endTextPrinting(final Word word, final TextView textView) {
        textView.removeCallbacks(word.runnable);
        textView.setText(word.word);
    }

    public static class Word {

        private long delayBetweenSymbols;
        private String word;
        private int offset;
        private int currentCharacterIndex;
        private Runnable runnable;

        public Word(long delayBetweenSymbols, String word) {
            this.currentCharacterIndex = 0;
            if (delayBetweenSymbols < 0) throw new IllegalArgumentException("Delay can't be < 0");
            this.delayBetweenSymbols = delayBetweenSymbols;
            this.word = word;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

}