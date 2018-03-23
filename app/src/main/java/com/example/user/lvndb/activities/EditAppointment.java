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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.lvndb.R;
import com.example.user.lvndb.classes.AlarmHandler;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.DataAppointment;
import com.example.user.lvndb.model.DataSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class EditAppointment extends AppCompatActivity {
    EditText etDoctor, etLocation, etNote;
    Button btnUpdate, btnDate, btnTime,btnDelete;
    Switch swActive;
    private static final int Date_id = 0;
    private static final int Time_id = 1;
    String doctorName,date,time, active="true";
    Calendar calendar;
    int code;
    int mYear, mMonth, mDay, mHour, mMinute;
    Bundle extras;
    ArrayList<DataAppointment> dataAppointments = new ArrayList<>();
    public static long dateInMilis;
    public static long timeInMilis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        final DBHelper dbHelper = new DBHelper(getApplicationContext());

        etDoctor = (EditText) findViewById(R.id.etEADoctor);
        etLocation = (EditText) findViewById(R.id.etEALocation);
        etNote = (EditText) findViewById(R.id.etEANote);
        btnUpdate = (Button) findViewById(R.id.btnUpdateAppointment);
        btnDelete=findViewById(R.id.btnDeleteAppointment);
        btnDate = (Button) findViewById(R.id.btnEADate);
        btnTime = findViewById(R.id.btnEATime);
        swActive=findViewById(R.id.swActiveAppointment);

        extras = getIntent().getExtras();
        // if (extras != null) {
        doctorName = extras.getString("doctorName");
        date = extras.getString("date");
        code=extras.getInt("code");

        //getting pill Info by Name
        dataAppointments = dbHelper.getAppointmentInfo(code);
        Log.d("Obj Data Check", dataAppointments.get(0).getCode() + " " + dataAppointments.get(0).getDoctorName() +" "+ dataAppointments.get(0).getDate() + " " + dataAppointments.get(0).getTime());

        //set switch value
        swActive.setChecked(Boolean.parseBoolean(dataAppointments.get(0).getActive()));
        swActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    active = "true";
                } else active = "false";
            }
        });
        //set values in fields
        setValues();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ShowPill Date dialog
                showDialog(Date_id);
            }
        });


        //AddPill Button Click
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctorName = etDoctor.getText().toString();
                String  location= etLocation.getText().toString();
                String  note= etNote.getText().toString();

                //AddPill into Database
                DataAppointment da = new DataAppointment(code,doctorName,location,date,time,note,active);
                Log.d("Appoint Data Check", doctorName + " " + location +" "+ date + " " + time+" "+active);
                dbHelper.updateAppointment(da);
                Toast.makeText(getApplicationContext(), "Appointment "+code+" updated for "+ time, Toast.LENGTH_SHORT).show();
                Log.d("Code Check", String.valueOf(code));


                //Trigger Alarm
                if (active.equals("true")) {
                    AlarmHandler alarmHandler = new AlarmHandler();
                    alarmHandler.startAppointmentAlarm(EditAppointment.this, doctorName, timeInMilis, (code + 10000));
                    Log.d("Code Check for alarm", String.valueOf(code + 10000));
                    Log.d("Time for alarm", String.valueOf(timeInMilis));
                }
                if (active.equals("false")) {
                    AlarmHandler alarmHandler = new AlarmHandler();
                    alarmHandler.cancelAppointment(EditAppointment.this,(code + 10000));
                    Log.d("Code Check for alarm", String.valueOf(code + 10000));
                    Log.d("Time for alarm", String.valueOf(timeInMilis));
                }

                Intent intent=new Intent(EditAppointment.this,ShowAppointment.class);
                startActivity(intent);
            }
        });
        //AddPill Button Click
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AddPill into Database
                dbHelper.deleteAppointment(code);
                Toast.makeText(getApplicationContext(), "Appointment "+code+" deleted for "+ time, Toast.LENGTH_SHORT).show();
                Log.d("Code Check", String.valueOf(code));

                //Trigger Alarm
                AlarmHandler alarmHandler = new AlarmHandler();
                alarmHandler.cancelAppointment(EditAppointment.this, (code+10000));
                Log.d("Code Check for alarm", String.valueOf(code+10000));
                Log.d("Time for alarm", String.valueOf(timeInMilis));
                Intent intent=new Intent(EditAppointment.this,ShowAppointment.class);
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
                return new DatePickerDialog(EditAppointment.this, date_listener, year,
                        month, day);
            case Time_id:

                // Open the timepicker dialog
                return new TimePickerDialog(EditAppointment.this, time_listener, hour,
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

    public void setValues(){
        //splitting date into parts
        String[] dateParts = dataAppointments.get(0).getDate().split("-");
        mDay = Integer.parseInt(dateParts[0]);
        mMonth = Integer.parseInt(dateParts[1]);
        mYear = Integer.parseInt(dateParts[2]);

        //set edit text value
        etDoctor.setText(dataAppointments.get(0).getDoctorName());
        etLocation.setText(dataAppointments.get(0).getLocation());
        etNote.setText(dataAppointments.get(0).getNote());

        String timeForBtn = mDay + "-" + (mMonth + 1) + "-" + mYear;
        btnDate.setText(timeForBtn);

        //get alarm time
        time = dataAppointments.get(0).getTime();
        btnTime.setText(time);

//get time in milis
        String datetime = date + " " + time;
        SimpleDateFormat sdtf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            java.util.Date mDate = sdtf.parse(datetime);
            timeInMilis = mDate.getTime();
            Log.d(TAG, "Date format: " + datetime);
            Log.d(TAG, "NextAlarm Milis: "+timeInMilis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    }

