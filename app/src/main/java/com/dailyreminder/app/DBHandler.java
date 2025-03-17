package com.dailyreminder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "eventdb";
    private static final int DB_VERSION = 6;
    private static final String TABLE_NAME = "myevents";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String NOTE_COL = "note";
    private static final String DATE_COL = "datetime";
    private static final String TIME_COL = "time";
    private static final String NOTIFICATION_COL = "notification";

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
                + NOTIFICATION_COL + " INTEGER DEFAULT 0)";

        db.execSQL(query);
    }

    public void addNewEvent(String eventName, String eventNote, String date, String time, boolean notification){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COL, eventName);
        values.put(NOTE_COL, eventNote);
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(NOTIFICATION_COL, notification ? 1 : 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<EventModel> readEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
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
                    formattedDate = readerFormat.format(dbFormat.parse(date));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                eventModelArrayList.add(new EventModel(cursorEvents.getInt(0),
                        cursorEvents.getString(1),
                        cursorEvents.getString(2),
                        formattedDate,
                        cursorEvents.getString(4),
                        cursorEvents.getInt(5) == 1));
            } while (cursorEvents.moveToNext());

        }
        cursorEvents.close();
        return eventModelArrayList;

    }
    public void updateEvent(String id, String eventName, String eventNote, String date, String time, boolean notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, eventName);
        values.put(NOTE_COL, eventNote);
        values.put(DATE_COL, date);
        values.put(TIME_COL, time);
        values.put(NOTIFICATION_COL, notification ? 1 : 0);
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
