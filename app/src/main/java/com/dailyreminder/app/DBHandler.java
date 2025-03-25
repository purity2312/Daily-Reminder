package com.dailyreminder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "eventdb";
    private static final int DB_VERSION = 17;
    private static final String TABLE_NAME = "myevents";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String NOTE_COL = "note";
    private static final String DATE_COL = "datetime";
    private static final String TIME_COL = "time";
    private static final String YEAR_COL = "year";
    private static final String MONTH_COL = "month";
    private static final String DAY_COL = "day";



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
                + DAY_COL + " INTEGER)";

        db.execSQL(query);
    }

    public void addNewEvent(String eventName, String eventNote, String date, String time, int year, int month, int day){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COL, eventName);
        values.put(NOTE_COL, eventNote);
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(YEAR_COL, year);
        values.put(MONTH_COL, month);
        values.put(DAY_COL, day);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<EventModel> readEvents() {
        Calendar cal = Calendar.getInstance();
        SQLiteDatabase db = this.getReadableDatabase();
        // work on this later to filter today/this month/ this year's tasks
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Cursor cursorEvents = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + DATE_COL + ", " + TIME_COL, null);
        ArrayList<EventModel> eventModelArrayList = new ArrayList<>();
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat readerFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate;
        String date;
        if (cursorEvents.moveToFirst()) {
            do {
                date = cursorEvents.getString(3);
                try {
                    formattedDate = readerFormat.format(Objects.requireNonNull(dbFormat.parse(date)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                eventModelArrayList.add(new EventModel(cursorEvents.getInt(0),
                        cursorEvents.getString(1),
                        cursorEvents.getString(2),
                        formattedDate,
                        cursorEvents.getString(4),
                        cursorEvents.getInt(5),
                        cursorEvents.getInt(6),
                        cursorEvents.getInt(7)));
            } while (cursorEvents.moveToNext());

        }
        cursorEvents.close();
        return eventModelArrayList;

    }
    public void updateEvent(String id, String eventName, String eventNote, String date, String time, int year, int month, int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, eventName);
        values.put(NOTE_COL, eventNote);
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(YEAR_COL, year);
        values.put(MONTH_COL, month);
        values.put(DAY_COL, day);
        db.update(TABLE_NAME, values, "id=?", new String[]{id});
        db.close();

    }

    public void deleteEvent(String id) {
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
