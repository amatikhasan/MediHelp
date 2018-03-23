package com.example.user.lvndb.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.lvndb.R;
import com.example.user.lvndb.classes.AlarmHandler;
import com.example.user.lvndb.db.DBAppointment;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.DataAppointment;

import java.util.Calendar;

public class AddAppointment extends AppCompatActivity {
    EditText etDoctor, etLocation, etNote;
    Button btnInsert, btnDate, btnTime;
    private static final int Date_id = 0;
    private static final int Time_id = 1;
    String date,time, active;
    Calendar calendar;
    int mYear, mMonth, mDay, mHour, mMinute;

    public static long dateInMilis;
    public static long timeInMilis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        final DBHelper dbHelper = new DBHelper(getApplicationContext());

        etDoctor = (EditText) findViewById(R.id.etAppointmentDoctor);
        etLocation = (EditText) findViewById(R.id.etAppointmentLocation);
        etNote = (EditText) findViewById(R.id.etAppointmentNote);
        btnInsert = (Button) findViewById(R.id.btnAddAppointment);
        btnDate = (Button) findViewById(R.id.btnAppointmentDate);
        btnTime = findViewById(R.id.btnAppointmentTime);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // ShowPill Date dialog
                showDialog(Date_id);
            }
        });


        //AddPill Button Click
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctorName = etDoctor.getText().toString();
                String  location= etLocation.getText().toString();
                String  note= etNote.getText().toString();
                active = "true";
                int code;

                //AddPill into Database
                DataAppointment da = new DataAppointment(doctorName,location,date,time,note,active);
                Log.d("Appoint Data Check", doctorName + " " + location +" "+ date + " " + time+" "+active);
                code =dbHelper.insertAppointmentData(da);
                Toast.makeText(getApplicationContext(), "Appointment "+code+" inserted for " + time, Toast.LENGTH_SHORT).show();
                Log.d("Appoint Code Check", String.valueOf(code));

                //Trigger Alarm
                AlarmHandler alarmHandler = new AlarmHandler();
                alarmHandler.startAppointmentAlarm(AddAppointment.this, doctorName, timeInMilis, (code+10000));
                Log.d("Code Check for alarm", String.valueOf(code+10000));
                Log.d("Time for alarm", String.valueOf(timeInMilis));
                Intent intent=new Intent(AddAppointment.this,ShowAppointment.class);
                startActivity(intent);
            }
    });
    }

    public void timeDialogue(View view) {
        // ShowPill Date dialog
        showDialog(Time_id);
    }

    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        switch (id) {
            case Date_id:

                // Open the datepicker dialog
                return new DatePickerDialog(AddAppointment.this, date_listener, year,
                        month, day);
            case Time_id:

                // Open the timepicker dialog
                return new TimePickerDialog(AddAppointment.this, time_listener, hour,
                        minute, false);

        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            calendar = Calendar.getInstance();
            mYear = year;
            mMonth = month;
            mDay = day;
            calendar.set(mYear, mMonth, mDay, mHour, mMinute,0);
            dateInMilis = calendar.getTimeInMillis();
            date = String.valueOf(mDay) + "-" + String.valueOf(mMonth)
                    + "-" + String.valueOf(mYear);
            String dateForBtn=String.valueOf(mDay) + "-" + String.valueOf(mMonth+1)
                    + "-" + String.valueOf(mYear);
            btnDate.setText(dateForBtn);
            Log.d("Date Check", date);
            Log.d("Date milis Check", String.valueOf(dateInMilis));
        }
    };

    // Time picker dialog
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            calendar = Calendar.getInstance();
                    mHour = hour;
                    mMinute = minute;

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute,0);

                    timeInMilis= calendar.getTimeInMillis();

                    if (minute < 10) {
                        time = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }

                    btnTime.setText(time);

            //String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            //set_time.setText(time1);
            Log.d("Time milis Check", String.valueOf(timeInMilis));
            Log.d("Date Check", time);
        }
    };


}
