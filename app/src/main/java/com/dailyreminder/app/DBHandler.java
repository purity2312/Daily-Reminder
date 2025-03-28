package com.dailyreminder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "taskdb";
    private static final int DB_VERSION = 19;
    private static final String TABLE_NAME = "mytasks";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String NOTE_COL = "note";
    private static final String DATE_COL = "datetime";
    private static final String TIME_COL = "time";
    private static final String YEAR_COL = "year";
    private static final String MONTH_COL = "month";
    private static final String DAY_COL = "day";
    private static final String COMPLETE_COL = "complete";


    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + NOTE_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + TIME_COL + " TEXT,"
                + YEAR_COL + " INTEGER,"
                + MONTH_COL + " INTEGER,"
                + DAY_COL + " INTEGER,"
                + COMPLETE_COL + " INTEGER DEFAULT 0)";

        db.execSQL(query);
    }

    public void addNewTask(String taskName, String taskNote, String date, String time, int year, int month, int day, boolean complete){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COL, taskName);
        values.put(NOTE_COL, taskNote);
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(YEAR_COL, year);
        values.put(MONTH_COL, month);
        values.put(DAY_COL, day);
        values.put(COMPLETE_COL, complete ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<TaskModel> readTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + DATE_COL + ", " + TIME_COL, null);
        ArrayList<TaskModel> taskModelArrayList = new ArrayList<>();

        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat readerFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate;
        String date;

        if (cursorTasks.moveToFirst()) {
            do {
                date = cursorTasks.getString(3);
                try {
                    formattedDate = readerFormat.format(Objects.requireNonNull(dbFormat.parse(date)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                taskModelArrayList.add(new TaskModel(cursorTasks.getInt(0),
                        cursorTasks.getString(1),
                        cursorTasks.getString(2),
                        formattedDate,
                        cursorTasks.getString(4),
                        cursorTasks.getInt(5),
                        cursorTasks.getInt(6),
                        cursorTasks.getInt(7),
                        cursorTasks.getInt(8) == 1));
            } while (cursorTasks.moveToNext());

        }
        cursorTasks.close();
        return taskModelArrayList;

    }
    public void updateTask(String id, String taskName, String taskNote, String date, String time, int year, int month, int day, boolean complete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, taskName);
        values.put(NOTE_COL, taskNote);
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(YEAR_COL, year);
        values.put(MONTH_COL, month);
        values.put(DAY_COL, day);
        values.put(COMPLETE_COL, complete ? 1: 0);
        db.update(TABLE_NAME, values, "id=?", new String[]{id});
        db.close();

    }

    public void deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{id});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
