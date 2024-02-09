package com.example.app.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    private SharedPreferences preferences;

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        initShared();
    }

    private void initShared(){
        preferences = context.getSharedPreferences(
                context.getPackageName() + ".database_versions",
                Context.MODE_PRIVATE
        );
    }

    private void saveVersion() { //Сохраняет последнюю версию
        preferences.edit().putInt(DB_NAME, DB_VERSION).apply();
    }

    private boolean checkVersion() { //Достает значение из SharedPreferences и сравнивает с версий базы данных
        return preferences.getInt(DB_NAME, 0) < DB_VERSION;
    }

    private void installOrUpdate() { //Проверяет, установлена ли база данных
        if (checkVersion()) {
            context.deleteDatabase(DB_NAME); //Удаляет базу данных если она Устарела
            installDatabase();
            saveVersion();
        }
    }

    private void installDatabase() { //Устанавливает базу данных из папки assets

        try {
            InputStream inputStream = context.getAssets().open(DB_NAME + ".db"); //Берем файл из assets
            File outputFile = new File(context.getDatabasePath(DB_NAME).getPath()); //Создаем пустой файл на телефоне
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            //перемещаем байты из входящего файла в исходящий
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer))>0){
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();

            outputStream.flush();
            outputStream.close();
        } catch (Exception exception) {
            throw new RuntimeException("The " + DB_NAME + " database couldn't be installed.", exception);
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() { //Срабатывает первым
        installOrUpdate();
        return super.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static final String DB_NAME = "story";
    public static final String TABLE_DIALOG = "dialog";
    public static final String TABLE_CHOICE = "choice";
    public static final int DB_VERSION = 1;
}
