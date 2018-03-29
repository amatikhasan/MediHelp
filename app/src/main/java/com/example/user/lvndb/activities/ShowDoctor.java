package com.example.user.lvndb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user.lvndb.R;
import com.example.user.lvndb.adapter.AppointmentAdapter;
import com.example.user.lvndb.adapter.DoctorAdapter;
import com.example.user.lvndb.db.DBExternal;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.AppointmentData;
import com.example.user.lvndb.model.DoctorData;

import java.util.ArrayList;

public class ShowDoctor extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DoctorData> obj = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctor);

        String speciality=getIntent().getExtras().getString("speciality");
        String district=getIntent().getExtras().getString("district");
        String location=getIntent().getExtras().getString("location");

        recyclerView = (RecyclerView) findViewById(R.id.rv_doctor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBExternal dbExternal = new DBExternal(getApplicationContext());

        obj = dbExternal.showDoctor(speciality,district,location);
        Log.d("Check Db results", "Db obj length: "+obj.size());
        DoctorAdapter adapter = new DoctorAdapter(this,obj);
        recyclerView.setAdapter(adapter);
    }
}
