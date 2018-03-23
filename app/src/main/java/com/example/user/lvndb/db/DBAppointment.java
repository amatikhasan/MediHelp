package com.example.user.lvndb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.lvndb.model.DataAppointment;
import com.example.user.lvndb.model.DataSchedule;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/21/2018.
 */

public class DBAppointment extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "mydb.db";
    private static String TABLE_NAME = "appointment";
    private static String ID = "id";
    private static String DoctorName = "doctorName";
    private static String Location = "location";
    private static String Note = "note";
    private static String Date = "date";
    private static String Time = "time";
    private static String Active = "active";

    //private static String Code = "code";

    public DBAppointment(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME + " ( " + ID + " integer primary key, " + DoctorName + " text not null, " + Location + " text not null, " + Date + " date not null, " + Time + " time not null," + Note + " text not null," + Active + " text not null)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = " Drop table if exists " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public int insertAppointmentData(DataAppointment da) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DoctorName, da.getDoctorName());
        values.put(Location, da.getLocation());
        values.put(Date, da.getDate());
        values.put(Time, da.getTime());
        values.put(Note, da.getNote());
        values.put(Active, da.getActive());

        long id = sd.insert(TABLE_NAME, null, values);
        sd.close();
        Log.d(TAG, String.valueOf(id));
        return (int) id;
    }

    public ArrayList<DataAppointment> showAppointment() {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select * from " + TABLE_NAME + " ";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<DataAppointment> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int code = cur.getInt(0);
                String doctorName = cur.getString(1);
                String location = cur.getString(2);
                String date = cur.getString(3);
                String time = cur.getString(4);
                String note = cur.getString(5);
                String active = cur.getString(6);
                data.add(new DataAppointment(code,doctorName,location,date,time,note,active));

            } while (cur.moveToNext());
        }
        cur.close();
        return data;
    }
}
