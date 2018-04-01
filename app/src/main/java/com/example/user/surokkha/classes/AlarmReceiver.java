package com.example.user.surokkha.classes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/11/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
Date date1,date2;
    @Override
    public void onReceive(Context context, Intent intent) {
        String pillName = intent.getExtras().getString("pillName");
        int code = intent.getExtras().getInt("code");
        String time = intent.getExtras().getString("time");
        String date = intent.getExtras().getString("date");
        int duration = intent.getExtras().getInt("duration");

        String doctorName = intent.getExtras().getString("doctorName");

        if (pillName != null) {
            checkDate(context,code,date,duration);
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();
        //Intent i=new Intent(context,AlarmActivity.class);
        //context.startActivity(i);
        Toast.makeText(context, "Testing AlarmReceiver:" + pillName + " " + code, Toast.LENGTH_SHORT).show();
        Log.d("Alarm Triggered:", pillName + " " + code);

    }


    public void checkDate(Context context,int code,String date,int duration) {
        // Get the calander
        Calendar c = Calendar.getInstance();
        // From calander get the year, month, day, hour, minute
        int today = c.get(Calendar.DAY_OF_MONTH);
        int thisMonth=c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);

        SimpleDateFormat sdtf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date1=sdtf.parse(String.valueOf(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "checkDate: "+date);
        Log.d(TAG, "check today Date: "+date1);

        String[] dateParts = date.split("-");
        int day = Integer.parseInt(dateParts[0]);
        int month=Integer.parseInt(dateParts[1]);

        if(month==2){
            if((day+duration)>28){
                month++;
                day=(day+duration)-28;
            }
            else day=(day+duration);
        }
       else if(month==4||month==6||month==9||month==11){
            if((day+duration)>30){
                month++;
                day=(day+duration)-30;
            }
            else day=(day+duration);
       }
       else {
            if((day+duration)>31){
                month++;
                day=(day+duration)-31;
            }
            else day=(day+duration);
       }

       String newDate=day+"-"+month+"-"+year;

        try {
            date2 = sdtf.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "check new Date: "+date2);

        if(date1.before(date2)){
            AlarmHandler alarmHandler=new AlarmHandler();
            alarmHandler.cancelAlarm(context,code);
            Toast.makeText(context, "Alarm cancelled:" + date + " " + code, Toast.LENGTH_SHORT).show();
        }

    }
}
