package com.dailyreminder.app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class EditEvent extends AppCompatActivity {

    private EditText eventNameEdt, eventNoteEdt;
    private Button dateButton, timeButton;
    private SwitchCompat eventNotificationSc;
    private DBHandler dbHandler;
    private DatePickerDialog datePickerDialog;
    private String eventId;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.AddEvent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initDatePicker();
        initTimePicker();
        dbHandler = new DBHandler(EditEvent.this);
        eventNameEdt = findViewById(R.id.editName);
        eventNoteEdt = findViewById(R.id.editNote);
        dateButton = findViewById(R.id.datePickerButton);
        timeButton = findViewById(R.id.timePickerButton);
        eventNotificationSc = findViewById(R.id.editNotification);
        dateButton.setText(getTodaysDate());
        timeButton.setText(getCurrentTime());

        if (getIntent().getStringExtra("event_id") != null) {
            eventId = getIntent().getStringExtra("event_id");
            eventNameEdt.setText(getIntent().getStringExtra("event_name"));
            eventNoteEdt.setText(getIntent().getStringExtra("event_note"));
            dateButton.setText(getIntent().getStringExtra("event_date"));
            timeButton.setText(getIntent().getStringExtra("event_time"));
            eventNotificationSc.setChecked(getIntent().getBooleanExtra("event_notification", false));
            setTitle("Update Event");
        } else {
            setTitle("Add Event");
        }

    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return hour + ":" + minute;
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String time = hour + ":" + minute;
                timeButton.setText(time);
            }
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
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }
    public void openDatePicker(View v) {
        datePickerDialog.show();
    }

    public void openTimePicker(View v) {
        timePickerDialog.show();
    }

    public void saveEvent(View v) throws ParseException {
        SimpleDateFormat readerFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String eventName = eventNameEdt.getText().toString();
        String eventNote = eventNoteEdt.getText().toString();
        String eventDate = dateButton.getText().toString();
        String eventTime = timeButton.getText().toString();
        String[] dateInfo = eventDate.split("/");
        int month = Integer.parseInt(dateInfo[0]);
        int day = Integer.parseInt(dateInfo[1]);
        int year = Integer.parseInt(dateInfo[2]);

        boolean eventNotification = eventNotificationSc.isChecked();
        String formattedDate = dbFormat.format(Objects.requireNonNull(readerFormat.parse(eventDate)));
        String formattedTime = timeFormat.format(Objects.requireNonNull(timeFormat.parse(eventTime)));

        if (getIntent().getStringExtra("event_id") != null) {
            dbHandler.updateEvent(eventId, eventName, eventNote, formattedDate, formattedTime, year, month, day, eventNotification);
            Toast.makeText(this, "Event has been updated.", Toast.LENGTH_SHORT).show();
        } else {
            dbHandler.addNewEvent(eventName, eventNote, formattedDate, formattedTime, year, month, day, eventNotification);
            Toast.makeText(this, "Event has been added.", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();


    }




}
