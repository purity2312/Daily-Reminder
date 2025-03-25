package com.dailyreminder.app;

public class TaskModel {

    private final int id;
    private final String taskName;
    private final String taskNote;
    private final String date;
    private final String time;


    public TaskModel(int id, String taskName, String taskNote, String date, String time, int year, int month, int day) {
        this.id = id;
        this.date = date;
        this.taskNote = taskNote;
        this.taskName = taskName;
        this.time = time;
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
}
