package com.example.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBDao {

    private SQLiteDatabase db;

    private List<DialogEntity> dialogs = new ArrayList<>();
    private List<ChoiceEntity> choice = new ArrayList<>();

    private DBDao(Context context) {
        db = new DBHelper(context).getReadableDatabase();
    }

    public void initDialog() {
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DIALOG, null); //Получаем все записи из таблицы

        if (c.moveToFirst()) { //Проверяем есть ли в ней элементы и переходим на первый

            //Получаем индексы столбцов
            int id = c.getColumnIndex(COLUMN_ID);
            int sentence = c.getColumnIndex(COLUMN_SENTENCE);
            int imageID = c.getColumnIndex(COLUMN_IMAGE_ID);
            int fonID = c.getColumnIndex(COLUMN_FON_ID);
            int side = c.getColumnIndex(COLUMN_SIDE);
            int act = c.getColumnIndex(COLUMN_ACT);
            do {
                String[] mas = c.getString(act).split("&"); //Разбиваем act на действие и индекс, если он есть
                int type = Integer.parseInt(mas[0]);
                int index = 0;
                if (mas.length > 1){
                    index = Integer.parseInt(mas[1]);
                }
                dialogs.add( //Создаем и добавляем новый объект в список
                        new DialogEntity(
                                c.getInt(id),
                                c.getString(sentence),
                                c.getString(imageID),
                                c.getString(fonID),
                                c.getInt(side),
                                type,
                                index
                        )
                );
            } while (c.moveToNext()); //Переходим к следующему элементу
        }
        c.close(); //Закрываем
    }

    public void initChoice(){
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHOICE, null);

        if (c.moveToFirst()) {
            int id = c.getColumnIndex(COLUMN_ID);
            int choice_sentence = c.getColumnIndex(COLUMN_CHOICE_SENTENCE);
            int transition = c.getColumnIndex(COLUMN_TRANSITION);
            do {
                List<String> listChoice = Arrays.asList(c.getString(choice_sentence).split("&"));

                String[] mas = c.getString(transition).split("&");
                List<Integer> listTrans = new ArrayList<>();
                for(String current: mas){
                    listTrans.add(Integer.parseInt(current));
                }

                choice.add(
                        new ChoiceEntity(
                                c.getInt(id),
                                listChoice,
                                listTrans
                        )
                );
            } while (c.moveToNext());
        }
        c.close();
    }

    public List<DialogEntity> getDialogs() {
        return dialogs;
    }

    public List<ChoiceEntity> getChoice() {
        return choice;
    }

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SENTENCE = "sentence";
    private static final String COLUMN_IMAGE_ID = "image_id";
    private static final String COLUMN_FON_ID = "fon_id";
    private static final String COLUMN_SIDE = "side";
    private static final String COLUMN_ACT = "act";

    private static final String COLUMN_CHOICE_SENTENCE = "choice_sentence";
    private static final String COLUMN_TRANSITION = "transition";

    /*Статическая переменная создается единожды для всех объектов класса,
    если создать 10 объектов(new DBDao()),
    то все будут иметь один общий instance*/
    private static DBDao instance;

    /*Если объект класса ещё ни разу не создавался,
    то инициализируем instance объектом класса,
    либо возвращаем уже существующий*/
    public static DBDao getInstance(Context context) {
        if (instance == null) {
            instance = new DBDao(context);
            return instance;
        }
        else return instance;
    }

    /*Это называется Singleton - один общий объект класса для всех инициализаций, нужен чтобы каждый раз не получать списки dialogs и choice*/
}
