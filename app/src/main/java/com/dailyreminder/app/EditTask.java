package com.dailyreminder.app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class EditTask extends AppCompatActivity {

    private EditText taskNameEdt, taskNoteEdt;
    private Button dateButton, timeButton;
    private DBHandler dbHandler;
    private DatePickerDialog datePickerDialog;
    private String taskId;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.AddTask), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initDatePicker();
        initTimePicker();
        dbHandler = new DBHandler(EditTask.this);
        taskNameEdt = findViewById(R.id.editName);
        taskNoteEdt = findViewById(R.id.editNote);
        dateButton = findViewById(R.id.datePickerButton);
        timeButton = findViewById(R.id.timePickerButton);
        dateButton.setText(getTodaysDate());
        timeButton.setText(getCurrentTime());

        if (getIntent().getStringExtra("task_id") != null) {
            taskId = getIntent().getStringExtra("task_id");
            taskNameEdt.setText(getIntent().getStringExtra("task_name"));
            taskNoteEdt.setText(getIntent().getStringExtra("task_note"));
            dateButton.setText(getIntent().getStringExtra("task_date"));
            timeButton.setText(getIntent().getStringExtra("task_time"));
            setTitle("Update Task");
        } else {
            setTitle("Add Task");
        }

    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return hour + ":" + minute;
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hour, minute) -> {
            String time = hour + ":" + minute;
            timeButton.setText(time);
        };
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, true);
    }


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return month + "/" + day + "/" + year;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = month + "/" + day + "/" + year;
            dateButton.setText(date);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }
    public void openDatePicker(View v) {
        datePickerDialog.show();
    }

    public void openTimePicker(View v) {
        timePickerDialog.show();
    }

    public void saveTask(View v) throws ParseException {
        SimpleDateFormat readerFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String taskName = taskNameEdt.getText().toString();
        String taskNote = taskNoteEdt.getText().toString();
        String taskDate = dateButton.getText().toString();
        String taskTime = timeButton.getText().toString();
        String[] dateInfo = taskDate.split("/");
        int month = Integer.parseInt(dateInfo[0]);
        int day = Integer.parseInt(dateInfo[1]);
        int year = Integer.parseInt(dateInfo[2]);

        String formattedDate = dbFormat.format(Objects.requireNonNull(readerFormat.parse(taskDate)));
        String formattedTime = timeFormat.format(Objects.requireNonNull(timeFormat.parse(taskTime)));

        if (getIntent().getStringExtra("task_id") != null) {
            dbHandler.updateTask(taskId, taskName, taskNote, formattedDate, formattedTime, year, month, day);
            Toast.makeText(this, "Task has been updated.", Toast.LENGTH_SHORT).show();
        } else {
            dbHandler.addNewTask(taskName, taskNote, formattedDate, formattedTime, year, month, day);
            Toast.makeText(this, "Task has been added.", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();


    }




}
