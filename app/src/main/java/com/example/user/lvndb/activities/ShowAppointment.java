package com.example.user.lvndb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.lvndb.R;
import com.example.user.lvndb.adapter.AppointmentAdapter;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.AppointmentData;

import java.util.ArrayList;

public class ShowAppointment extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<AppointmentData> obj = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment);

        recyclerView = (RecyclerView) findViewById(R.id.rvAppointment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        obj = dbHelper.showAppointment();

        AppointmentAdapter adapter = new AppointmentAdapter(this,obj);
        recyclerView.setAdapter(adapter);

    }
}
