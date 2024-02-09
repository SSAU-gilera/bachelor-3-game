package com.gilera.app.anim;

import android.animation.ObjectAnimator;
import android.view.View;

public class TransitionAnim {

    private View transition;
    public ObjectAnimator animator;
    private long duration;
    private long delay;

    public TransitionAnim(View transition, long duration, long delay) {
        this.transition = transition;
        this.duration = duration;
        this.delay = delay;
    }

    public void hideAnim() {
        animator = ObjectAnimator.ofFloat(transition, "alpha", 1f, 0f); //Изменение прозрачности от 100 до 0
        animator.setDuration(duration); //Время изменения
        animator.setStartDelay(delay); //Задержка перед стартом
        animator.start();
    }

    public void showAnim() {
        animator = ObjectAnimator.ofFloat(transition, "alpha", 0f, 1f);
        animator.setDuration(duration);
        animator.start();
    }
}