package com.dailyreminder.app;

public class EventModel {

    private final int id;
    private final String eventName;
    private final String eventNote;
    private final String date;
    private final String time;


    public EventModel(int id, String eventName, String eventNote, String date, String time, int year, int month, int day) {
        this.id = id;
        this.date = date;
        this.eventNote = eventNote;
        this.eventName = eventName;
        this.time = time;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventNote() {
        return eventNote;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }
}
