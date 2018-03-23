package com.example.user.lvndb.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.lvndb.R;
import com.example.user.lvndb.classes.AlarmHandler;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.DataSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class EditAlarm extends AppCompatActivity {
    EditText etPill, etQty, etUnit, etDuration;
    Button insert, btnDate, time1, time2, time3, time4,btnDelete;
    Spinner spNo, spFrequency;
    Switch swActive;
    private static final int Date_id = 0;
    private static final int Time_id = 1;
    Bundle extras;
    String[] frequency;
    String[] reminderNo;
    String pillName, date, days, active = "true", repeat;
    public static String[] time = new String[4];
    static int repeatNo;
    int timeBtnId;
    Calendar calendar;
    int mYear, mMonth, mDay, mHour, mMinute, year, month, day, hour, minute;
    ArrayList<DataSchedule> dataSchedules = new ArrayList<>();
    public static long dateInMilis;
    public static long[] timeInMilis = new long[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        final DBHelper dbHelper = new DBHelper(getApplicationContext());

        etPill = (EditText) findViewById(R.id.etPN);
        etQty = (EditText) findViewById(R.id.etQty);
        etUnit = (EditText) findViewById(R.id.etUnit);
        etDuration = (EditText) findViewById(R.id.etDuration);
        swActive = findViewById(R.id.swActive);
        insert = (Button) findViewById(R.id.Insert);
        btnDelete=findViewById(R.id.btnDeleteAlarm);
        btnDate = (Button) findViewById(R.id.btnStartDate);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        spNo = findViewById(R.id.spNo);
        spFrequency = findViewById(R.id.spFrequency);

        //spinner adapter
        frequency = getResources().getStringArray(R.array.frequency);
        reminderNo = getResources().getStringArray(R.array.reminderNo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.spText, frequency);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.spText, reminderNo);
        spFrequency.setAdapter(adapter);
        spNo.setAdapter(adapter2);

        //getting extras
        extras = getIntent().getExtras();
        // if (extras != null) {
        pillName = extras.getString("pillName");
        repeatNo = extras.getInt("repeatNo");
        date = extras.getString("date");

        //getting pill Info by Name
        dataSchedules = dbHelper.getPillInfo(pillName);


        //splitting date into parts
        String[] dateParts = dataSchedules.get(0).getDate().split("-");
        mDay = Integer.parseInt(dateParts[0]);
        mMonth = Integer.parseInt(dateParts[1]);
        mYear = Integer.parseInt(dateParts[2]);


        //set edit text value
        etPill.setText(dataSchedules.get(0).getPillName());
        etQty.setText(String.valueOf(dataSchedules.get(0).getQty()));
        etUnit.setText(dataSchedules.get(0).getUnit());
        etDuration.setText(String.valueOf(dataSchedules.get(0).getDuration()));
        spNo.setSelection(repeatNo - 1);
        String timeForBtn = mDay + "-" + (mMonth + 1) + "-" + mYear;
        btnDate.setText(timeForBtn);

        //getting timepreset
        getTime(repeatNo);

        //set switch value
        swActive.setChecked(Boolean.parseBoolean(dataSchedules.get(0).getActive()));
        swActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    active = "true";
                } else active = "false";
            }
        });

        //Spinner onItemSelect
        spNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeatNo = Integer.parseInt(spNo.getSelectedItem().toString());
                switch (repeatNo) {
                    case 1:
                        time1.setVisibility(View.VISIBLE);
                        time2.setVisibility(View.GONE);
                        time3.setVisibility(View.GONE);
                        time4.setVisibility(View.GONE);
                        break;
                    case 2:
                        time1.setVisibility(View.VISIBLE);
                        time2.setVisibility(View.VISIBLE);
                        time3.setVisibility(View.GONE);
                        time4.setVisibility(View.GONE);
                        break;
                    case 3:
                        time1.setVisibility(View.VISIBLE);
                        time2.setVisibility(View.VISIBLE);
                        time3.setVisibility(View.VISIBLE);
                        time4.setVisibility(View.GONE);
                        break;
                    case 4:
                        time1.setVisibility(View.VISIBLE);
                        time2.setVisibility(View.VISIBLE);
                        time3.setVisibility(View.VISIBLE);
                        time4.setVisibility(View.VISIBLE);
                        break;
                }
                Log.d("Count Check", String.valueOf(repeatNo));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Spinner onItemSelect
        spFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selected = spFrequency.getSelectedItemPosition();
                switch (selected) {
                    case 0:
                        days = "Everyday";
                        break;
                    case 1:
                        break;
                }
                Log.d("Day Check", String.valueOf(selected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //AddPill Button Click
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pillName = etPill.getText().toString();
                int qty = Integer.parseInt(etQty.getText().toString());
                String unit = etUnit.getText().toString();
                int duration = Integer.parseInt(etDuration.getText().toString());
                int id;
                //AddPill into Database
                for (int i = 0; i < repeatNo; i++) {
                    id = dataSchedules.get(i).getCode();
                    DataSchedule ds = new DataSchedule(id, pillName, qty, unit, duration, days, date, time[i], repeatNo, active);
                    Log.d("Data Check for update", pillName + " " + qty + " " + unit + " " + duration + " " + days + " " + date + " " + repeatNo + " " + time[i]);
                    dbHelper.updatePillData(ds);
                    Toast.makeText(getApplicationContext(), "Pill " + (i + 1) + " Updated for " + time[i], Toast.LENGTH_SHORT).show();
                }
                Log.d("Code length", String.valueOf(repeatNo));
                //Trigger Alarm
                if (active.equals("true")) {
                    for (int i = 0; i < repeatNo; i++) {
                        id = dataSchedules.get(i).getCode();
                        AlarmHandler alarmHandler = new AlarmHandler();
                        alarmHandler.cancelAlarm(EditAlarm.this, id);
                        alarmHandler.startAlarm(EditAlarm.this, pillName, timeInMilis[i], id);
                        Log.d("Code Check for alarm", String.valueOf(id));
                        Log.d("Date for alarm", String.valueOf(date));
                        Log.d("Time for alarm", String.valueOf(time[i]));
                        Log.d("TimeMilis for alarm", String.valueOf(timeInMilis[i]));
                    }
                }
                if (active.equals("false")) {
                    for (int i = 0; i < repeatNo; i++) {
                        id = dataSchedules.get(i).getCode();
                        AlarmHandler alarmHandler = new AlarmHandler();
                        alarmHandler.cancelAlarm(EditAlarm.this, id);
                        Log.d("Code for CancelAlarm", String.valueOf(id));
                        Log.d("Date for CancelAlarm", String.valueOf(date));
                        Log.d("Time for CancelAlarm", String.valueOf(time[i]));
                        Log.d("TimeMilis for Cancel", String.valueOf(timeInMilis[i]));
                    }
                }

            }
        });

        //Delete Button Click
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pillName = etPill.getText().toString();
                int id;
                //AddPill into Database
                for (int i = 0; i < repeatNo; i++) {
                    id = dataSchedules.get(i).getCode();
                    //pillName=dataSchedules.get(0).getPillName();
                    dbHelper.deleteAlarm(id);
                    Toast.makeText(getApplicationContext(), "Data " + (i + 1) + " succesfully deleted for " + pillName, Toast.LENGTH_SHORT).show();
                }
                //Cancel Alarm
                    for (int i = 0; i < repeatNo; i++) {
                        id = dataSchedules.get(i).getCode();
                        AlarmHandler alarmHandler = new AlarmHandler();
                        alarmHandler.cancelAlarm(EditAlarm.this, id);
                        Log.d("Code for delete alarm", String.valueOf(id));
                        Log.d("Date for delete alarm", String.valueOf(date));
                        Log.d("Time for delete alarm", String.valueOf(time[i]));
                        Log.d("Milis for delete alarm", String.valueOf(timeInMilis[i]));
                }
            }
        });


        //Date button click
        btnDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // ShowPill Date dialog
                showDialog(Date_id);
            }
        });

    }

    //Time Button click

    public void timeDialogue(View view) {
        timeBtnId = view.getId();

        // ShowPill Date dialog
        showDialog(Time_id);
    }

    //Method for showing Dialogue
    protected Dialog onCreateDialog(int id) {

        int count=extras.getInt("repeatNo");;

        //calling dialogue for date and time
        switch (id) {
            case Date_id:

                // Open the datepicker dialog
                return new DatePickerDialog(EditAlarm.this, date_listener, mYear,
                        mMonth, mDay);

            case Time_id:

                    if (timeBtnId == R.id.time1) {
                        //splitting time into parts
                        String[] timeParts = dataSchedules.get(0).getTime().split(":");
                        hour = Integer.parseInt(timeParts[0]);
                        minute = Integer.parseInt(timeParts[1]);
                    }
                    else if (timeBtnId == R.id.time2) {
                        //splitting time into parts

                        if(count>=2){
                            String[] timeParts = dataSchedules.get(1).getTime().split(":");
                            hour = Integer.parseInt(timeParts[0]);
                            minute = Integer.parseInt(timeParts[1]);
                        }
                    }
                    else if (timeBtnId == R.id.time3) {

                        if(count>=3) {
                            //splitting time into parts
                            String[] timeParts = dataSchedules.get(2).getTime().split(":");
                            hour = Integer.parseInt(timeParts[0]);
                            minute = Integer.parseInt(timeParts[1]);
                        }

                    }

                   else if (timeBtnId == R.id.time4) {
                        if(count==4) {
                            //splitting time into parts
                            String[] timeParts = dataSchedules.get(3).getTime().split(":");
                            hour = Integer.parseInt(timeParts[0]);
                            minute = Integer.parseInt(timeParts[1]);
                        }
                    }
                    Log.d("Time Button ID", String.valueOf(timeBtnId) + " " + R.id.time1);
                    // Open the timepicker dialog
                    return new TimePickerDialog(EditAlarm.this, time_listener, hour,
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
            date = String.valueOf(day) + "-" + String.valueOf(month + 1)
                    + "-" + String.valueOf(year);
            String dateForBtn = String.valueOf(mDay) + "-" + String.valueOf(mMonth + 1)
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
            String[] timeParts;
            calendar = Calendar.getInstance();

            switch (timeBtnId) {
                case R.id.time1:
                    timeParts = dataSchedules.get(0).getTime().split(":");
                    mHour = Integer.parseInt(timeParts[0]);
                    mMinute = Integer.parseInt(timeParts[1]);
                    //mHour = hour;
                    //mMinute = minute;
                    Log.d("timeIn", hour + ":" + minute);

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute,0);
                    Log.d("timeInMilis length", String.valueOf(timeInMilis.length));

                    timeInMilis[0] = calendar.getTimeInMillis();
                    Log.d("timeInMilis 0", String.valueOf(timeInMilis[0]));
                    if (minute < 10) {
                        time[0] = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time[0] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }

                    time1.setText(time[0]);
                    break;

                case R.id.time2:
                    timeParts = dataSchedules.get(1).getTime().split(":");
                    mHour = Integer.parseInt(timeParts[0]);
                    mMinute = Integer.parseInt(timeParts[1]);
                    //mHour = hour;
                    //mMinute = minute;

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute,0);
                    timeInMilis[1] = calendar.getTimeInMillis();
                    Log.d("timeInMilis 1", String.valueOf(timeInMilis[1]));
                    if (minute < 10) {
                        time[1] = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time[1] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }

                    time2.setText(time[1]);
                    break;
                case R.id.time3:

                    mHour = hour;
                    mMinute = minute;

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute,0);
                    timeInMilis[2] = calendar.getTimeInMillis();

                    if (minute < 10) {
                        time[2] = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time[2] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }
                    time3.setText(time[2]);
                    break;
                case R.id.time4:

                    mHour = hour;
                    mMinute = minute;

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute,0);
                    timeInMilis[3] = calendar.getTimeInMillis();

                    if (minute < 10) {
                        time[3] = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time[3] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }
                    time4.setText(time[3]);
                    break;
            }
            //String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            //set_time.setText(time1);
            //Log.d("Time milis Check", String.valueOf(timeInMilis[0]));
            Log.d("Date Check", time[0] + " " + time[1] + " " + time[2] + " " + time[3]);
        }
    };

    ////set spinner values and times
    public void getTime(int repeatNo) {
        if (repeatNo == 1) {
            time1.setVisibility(View.VISIBLE);
            time1.setText(dataSchedules.get(0).getTime());
            getTimeMilis(0);

        }
        if (repeatNo == 2) {
            time1.setVisibility(View.VISIBLE);
            time2.setVisibility(View.VISIBLE);
            time1.setText(dataSchedules.get(0).getTime());
            time2.setText(dataSchedules.get(1).getTime());
            getTimeMilis(0);
            getTimeMilis(1);

        }
        if (repeatNo == 3) {
            time1.setVisibility(View.VISIBLE);
            time2.setVisibility(View.VISIBLE);
            time3.setVisibility(View.VISIBLE);
            time1.setText(dataSchedules.get(0).getTime());
            time2.setText(dataSchedules.get(1).getTime());
            time3.setText(dataSchedules.get(2).getTime());
            getTimeMilis(0);
            getTimeMilis(1);
            getTimeMilis(2);

        }
        if (repeatNo == 4) {
            time1.setVisibility(View.VISIBLE);
            time2.setVisibility(View.VISIBLE);
            time3.setVisibility(View.VISIBLE);
            time4.setVisibility(View.VISIBLE);
            time1.setText(dataSchedules.get(0).getTime());
            time2.setText(dataSchedules.get(1).getTime());
            time3.setText(dataSchedules.get(2).getTime());
            time3.setText(dataSchedules.get(3).getTime());
            getTimeMilis(0);
            getTimeMilis(1);
            getTimeMilis(2);
            getTimeMilis(3);
        }
        Log.d(TAG, "NextAlarm Time: " + time[0] + " " + timeInMilis[0]);
    }

    public void getTimeMilis(int code) {

        //get alarm time
        time[code] = dataSchedules.get(code).getTime();

//get time in milis
        String datetime = date + " " + dataSchedules.get(code).getTime();
        SimpleDateFormat sdtf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            java.util.Date mDate = sdtf.parse(datetime);
            timeInMilis[code] = mDate.getTime();
            Log.d(TAG, "Date format: " + datetime);
            Log.d(TAG, "NextAlarm Milis: " + code + " " + timeInMilis[code]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
