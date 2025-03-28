package com.dailyreminder.app;

public class TaskModel {

    private final int id;
    private final String taskName;
    private final String taskNote;
    private final String date;
    private final String time;
    private final int year;
    private final int month;
    private final int day;
    private final boolean complete;

    public TaskModel(int id, String taskName, String taskNote, String date, String time, int year, int month, int day, boolean complete) {
        this.id = id;
        this.date = date;
        this.taskNote = taskNote;
        this.taskName = taskName;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.complete = complete;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskNote() {
        return taskNote;
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

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
    public boolean isComplete() {
        return complete;
    }
}
