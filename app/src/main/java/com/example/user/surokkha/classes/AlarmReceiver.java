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

import java.util.ArrayList;

/**
 * Created by User on 3/11/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String pillName=intent.getExtras().getString("pillName");
        int code=intent.getExtras().getInt("code");
        MediaPlayer mediaPlayer=MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();
        //Intent i=new Intent(context,AlarmActivity.class);
        //context.startActivity(i);
        Toast.makeText(context,"Testing AlarmReceiver:"+pillName+" "+code,Toast.LENGTH_SHORT).show();
        Log.d("Alarm Triggered:", pillName+" "+code);

    }
}
