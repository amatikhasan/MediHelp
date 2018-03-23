package com.example.user.lvndb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.lvndb.R;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.DataSchedule;
import com.example.user.lvndb.adapter.PillAdapter;

import java.util.ArrayList;

public class ShowPill extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DataSchedule> obj = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pill);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        obj = dbHelper.showPill();

        PillAdapter adapter = new PillAdapter(this,obj);
        recyclerView.setAdapter(adapter);

    }
}


