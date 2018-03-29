package com.example.user.lvndb.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.lvndb.R;
import com.example.user.lvndb.adapter.NoteAdapter;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.NoteData;

import java.util.ArrayList;

public class ShowNote extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<NoteData> obj = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        recyclerView = (RecyclerView) findViewById(R.id.rv_note);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        String name = getIntent().getStringExtra("name");

        obj = dbHelper.showNote();

        NoteAdapter adapter = new NoteAdapter(this,obj);
        recyclerView.setAdapter(adapter);
    }
}
