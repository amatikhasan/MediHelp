package com.example.user.lvndb.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.lvndb.R;
import com.example.user.lvndb.classes.AlarmHandler;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.PillData;

import java.util.*;

public class AddPill extends AppCompatActivity {
    EditText etPill, etQty, etUnit, etDuration;
    Button insert, btnDate, time1, time2, time3, time4;
    Spinner spNo, spFrequency;
    private static final int Date_id = 0;
    private static final int Time_id = 1;
    String[] frequency;
    String[] reminderNo;
    String date, active, repeat;
    String days;
    public static String[] time = new String[4];
    static int repeatNo;
    int timeBtnId;
    Calendar calendar;
    int mYear, mMonth, mDay, mHour, mMinute;
    DBHelper dbHelper;
    public static long dateInMilis;
    public static long[] timeInMilis = new long[4];
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());

        etPill = (EditText) findViewById(R.id.etPN);
        etQty = (EditText) findViewById(R.id.etQty);
        etUnit = (EditText) findViewById(R.id.etUnit);
        etDuration = (EditText) findViewById(R.id.etDuration);
        //insert = (Button) findViewById(R.id.Insert);
        btnDate = (Button) findViewById(R.id.btnStartDate);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        spNo = findViewById(R.id.spNo);
        spFrequency = findViewById(R.id.spFrequency);

        //Spinner Adapter
        frequency = getResources().getStringArray(R.array.frequency);
        reminderNo = getResources().getStringArray(R.array.reminderNo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.spText, frequency);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.spText, reminderNo);
        spFrequency.setAdapter(adapter);
        spNo.setAdapter(adapter2);

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

        /*
        //AddPill Button Click
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pillName = etPill.getText().toString();
                int qty = Integer.parseInt(etQty.getText().toString());
                String unit = etUnit.getText().toString();
                int duration = Integer.valueOf(etDuration.getText().toString());
                active = "true";
                int[] code = new int[repeatNo];
                //code = dbHelper.getPillCode();

                //AddPill into Database
                for (int i = 0; i < repeatNo; i++) {
                    PillData ds = new PillData(pillName, qty, unit, duration, days, date, time[i], repeatNo, active);
                    Log.d("Data Check for pill", pillName + " " + qty + " " + unit + " " + duration + " " + days + " " + date + " " + repeatNo + " " + time[i]);
                    code[i] = dbHelper.insertPillData(ds);
                    Toast.makeText(getApplicationContext(), "Pill " + (i + 1) + " inserted for " + time[i], Toast.LENGTH_SHORT).show();
                    Log.d("Code Check for pill", String.valueOf(code[i]));
                }
                Log.d("pill inserted", String.valueOf(code.length));
                //Trigger Alarm
                for (int i = 0; i < code.length; i++) {
                    AlarmHandler alarmHandler = new AlarmHandler();
                    alarmHandler.startAlarm(AddPill.this, pillName, timeInMilis[i], code[i]);
                    Log.d("Code Check for alarm", String.valueOf(code[i]));
                    Log.d("Time for alarm","Pill:"+code[i]+" "+ String.valueOf(timeInMilis[i]));

                    //Intent intent=new Intent(AddPill.this,ShowPill.class);
                    //startActivity(intent);
                }

            }
        });
*/
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
                return new DatePickerDialog(AddPill.this, date_listener, year,
                        month, day);
            case Time_id:

                // Open the timepicker dialog
                return new TimePickerDialog(AddPill.this, time_listener, hour,
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
            calendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
            dateInMilis = calendar.getTimeInMillis();

            date = String.valueOf(mDay) + "-" + String.valueOf(mMonth + 1)
                    + "-" + String.valueOf(mYear);
            btnDate.setText(date);
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
            Log.d("Time Button Check", timeBtnId + " " + R.id.time1);
            switch (timeBtnId) {
                case R.id.time1:
                    mHour = hour;
                    mMinute = minute;

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
                    Log.d("timeInMilis length", String.valueOf(timeInMilis.length));
                    timeInMilis[0] = calendar.getTimeInMillis();

                    if (minute < 10) {
                        time[0] = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time[0] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }

                    time1.setText(time[0]);
                    break;

                case R.id.time2:
                    mHour = hour;
                    mMinute = minute;

                    calendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
                    timeInMilis[1] = calendar.getTimeInMillis();

                    if (minute < 10) {
                        time[1] = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                    } else {
                        time[1] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    }

                    time2.setText(time[1]);
                    break;
                case R.id.time3:
                    time[2] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    time3.setText(time[2]);
                    break;
                case R.id.time4:
                    time[3] = String.valueOf(hour) + ":" + String.valueOf(minute);
                    time4.setText(time[3]);
                    break;
            }
            //String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            //set_time.setText(time1);
            Log.d("Time milis Check", String.valueOf(timeInMilis[0]));
            Log.d("Date Check", time[0] + " " + time[1] + " " + time[2] + " " + time[3]);
        }
    };

    //For Action Bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "back Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuSave:
                addPill();
                //Toast.makeText(getApplicationContext(), "Save Button Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    public void addPill() {
        String pillName = etPill.getText().toString();
        int qty = Integer.parseInt(etQty.getText().toString());
        String unit = etUnit.getText().toString();
        int duration = Integer.valueOf(etDuration.getText().toString());
        active = "true";
        int[] code = new int[repeatNo];
        //code = dbHelper.getPillCode();

        //AddPill into Database
        for (int i = 0; i < repeatNo; i++) {
            PillData ds = new PillData(pillName, qty, unit, duration, days, date, time[i], repeatNo, active);
            Log.d("Data Check for pill", pillName + " " + qty + " " + unit + " " + duration + " " + days + " " + date + " " + repeatNo + " " + time[i]);
            code[i] = dbHelper.insertPillData(ds);
            Toast.makeText(getApplicationContext(), "Pill " + (i + 1) + " inserted for " + time[i], Toast.LENGTH_SHORT).show();
            Log.d("Code Check for pill", String.valueOf(code[i]));
        }
        Log.d("pill inserted", String.valueOf(code.length));
        //Trigger Alarm
        for (int i = 0; i < code.length; i++) {
            AlarmHandler alarmHandler = new AlarmHandler();
            alarmHandler.startAlarm(AddPill.this, pillName, timeInMilis[i], code[i]);
            Log.d("Code Check for alarm", String.valueOf(code[i]));
            Log.d("Time for alarm", "Pill:" + code[i] + " " + String.valueOf(timeInMilis[i]));

            //Intent intent=new Intent(AddPill.this,ShowPill.class);
            //startActivity(intent);
        }
    }
}
