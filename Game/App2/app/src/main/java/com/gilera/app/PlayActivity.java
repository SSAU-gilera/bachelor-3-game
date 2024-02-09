package com.gilera.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gilera.app.anim.TextAnim;
import com.gilera.app.anim.TransitionAnim;
import com.gilera.app.choice.ChoiceFragment;
import com.gilera.app.db.DBDao;
import com.gilera.app.db.DialogEntity;

import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private TextView textView; //Поле ввода
    private ImageView message; //Диалоговое окно
    private ConstraintLayout fon; //Фон
    private ImageView character; //Персонаж
    private View blackout; //Затемнение

    private TextAnim textAnim; //Анимация текста
    private TransitionAnim blackoutAnim; //Анимация затемнения
    private TransitionAnim replaceAnim; //Анимация смены персонажа

    private boolean actionReplace = false; //Убирается ли персонаж
    private boolean actionBlackout = false; //Убирается ли затемнение
    private boolean actionText = false; //Печатается ли текст
    public int indexDialog = 0; //Индекс записи
    private int indexChoice = 0; //Индекс записи выбора
    private List<DialogEntity> dialogs; //Все записи
    private int act; //Флаг действия

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        textView = findViewById(R.id.text_start);
        blackout = findViewById(R.id.blackout);
        fon = findViewById(R.id.fon);
        character = findViewById(R.id.character);
        message = findViewById(R.id.message);

        blackoutAnim = new TransitionAnim(blackout, 2000, 500);
        replaceAnim = new TransitionAnim(character, 1500, 500);

        dialogs = DBDao.getInstance(this).getDialogs();

        updateView();

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) { //Срабатывает каждый введенный символ
                if (s.length() > 0 && s.charAt(s.length() - 1) == '\0') { //Срабатывает когда был введен последний символ строки
                    if (act == 1) { //Окно выбора
                        ChoiceFragment.init(indexChoice).show(getSupportFragmentManager(), ChoiceFragment.TAG);
                    } else if (act == 2) { //Затемнение экрана
                        blackoutAnim.showAnim();
                        transitionBlackout();
                    } else if (act == 3) { //Завершение сюжета
                        Toast.makeText(getApplicationContext(), "Конец", Toast.LENGTH_LONG).show();
                        //finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //True если это нажатие(Второй раз срабатывает когда отпускаешь)
            if (!actionText) { //Ускорить смену текста
                actionText = true;
                textAnim.cancel(false);
            } else {
                if (textAnim.getStatus() == AsyncTask.Status.FINISHED && act == 0) { //Проверка, что текст допечатался и после него нет действия
                    actionText = false;
                    if (dialogs.get(indexDialog).getImage_id().equals("0")){ //Проверка, что следующая запись не требует смены персонажа
                        updateView();
                    } else {
                        if (indexDialog > 0){ //Анимация смены персонажа
                            replaceAnim.hideAnim();
                            transitionReplace();
                        }
                    }
                }
            }
            if(replaceAnim.animator != null && replaceAnim.animator.isRunning()){ //Ускорить смену персонажа
                replaceAnim.animator.end();
            }
        }
        return true;
    }

    private void updateView() {
        DialogEntity dialogEntity = dialogs.get(indexDialog);
        String sentence = dialogEntity.getSentence() + '\0';
        String image_id = dialogEntity.getImage_id();
        String fon_id = dialogEntity.getFon_id();
        int side = dialogEntity.getSide();
        act = dialogEntity.getAct();
        indexChoice = dialogEntity.getIndex() - 1;
        if (!image_id.equals("0")) { //Установка персонажа
            if (!image_id.equals("-1")) {
                int id = getResources().getIdentifier(image_id, "drawable", getPackageName());
                character.setImageResource(id);
            } else {
                character.setImageResource(R.color.colorTransparent);
            }
        }
        if (!fon_id.equals("0")) { //Установка фона
            int id = getResources().getIdentifier(fon_id, "drawable", getPackageName());
            fon.setBackgroundResource(id);
        }
        if (side != 0) { //Установка стороны(персонажа и диалогового окна)
            if (side == 1) {
                character.setScaleType(ImageView.ScaleType.FIT_START);
                message.setImageResource(R.drawable.text_left);
            } else if (side == -1) {
                message.setImageResource(R.color.colorTransparent);
            } else {
                character.setScaleType(ImageView.ScaleType.FIT_END);
                message.setImageResource(R.drawable.text_right);
            }
        }
        if (sentence.charAt(0) != '0') { //Установка текста
            textAnim(sentence);
        } else {
            textView.setText(textView.getText());
        }
        indexDialog++;
    }

    public void resultChoice(int index) { //Результат выбора
        actionText = false;
        indexDialog = index;
        if (dialogs.get(indexDialog).getImage_id().equals("0")){ //Проверка, что следующая запись не требует смены персонажа
            updateView();
        } else {
            if (indexDialog > 0){ //смена персонажа
                replaceAnim.hideAnim();
                transitionReplace();
            }
        }
    }

    private void textAnim(String sentence) { //Анимация текста
        textView.setText("");
        textAnim = new TextAnim(textView, sentence);
        textAnim.execute();
    }

    private void transitionBlackout() { //Анимация затемнения
        blackoutAnim.animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) { //Когда заканчивается анимация
                if (!actionBlackout) {
                    blackoutAnim.hideAnim();
                    transitionBlackout();
                    actionBlackout = true;
                    updateView();
                } else {
                    actionBlackout = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void transitionReplace() { //Анимация смены персонажа

        replaceAnim.animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) { //Когда заканчивается анимация
                if (!actionReplace) {
                    updateView();
                    replaceAnim.showAnim();
                    transitionReplace();
                    actionReplace = true;
                } else {
                    actionReplace = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
