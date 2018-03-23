package com.example.user.lvndb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.lvndb.R;
import com.example.user.lvndb.db.DBHelper;
import com.example.user.lvndb.model.DataAppointment;
import com.example.user.lvndb.model.DataNote;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {
    EditText etTitle, etNote;
    Button btnDelete;
    int id;
    String title, note, date, time;
    Boolean isUpdate = false;
    DBHelper dbHelper;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dbHelper = new DBHelper(getApplicationContext());

        etNote = findViewById(R.id.etNoteBody);
        etTitle = findViewById(R.id.etNoteTitle);
        btnDelete = findViewById(R.id.btnDeleteNote);
        etNote.requestFocus();

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            title = extras.getString("title");
            note = extras.getString("note");
            isUpdate = true;
            etTitle.setText(title);
            etNote.setText(note);
            btnDelete.setVisibility(View.VISIBLE);
        }
        Log.d("Extra Data Check", id + " " + title + " " +isUpdate);


    }

    public void saveNote(View view) {
        title = etTitle.getText().toString();
        note = etNote.getText().toString();
        int code;

        // From calander get the year, month, day, hour, minute
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        date = day + "-" + (month + 1) + "-" + year;
        time = hour + ":" + minute;

        //Add Note into Database

        Log.d("Appoint Data Check", title + " " + note + " " + date + " " + time);

        if(!isUpdate) {
            DataNote dn = new DataNote(title, note, date, time);
            code = dbHelper.addNote(dn);
            Toast.makeText(getApplicationContext(), "Note "+code+" Added", Toast.LENGTH_SHORT).show();
        }
        if(isUpdate){
            DataNote dn = new DataNote(id,title, note, date, time);
            dbHelper.updateNote(dn);
            Toast.makeText(getApplicationContext(), "Note "+id+" Updated", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, ShowNote.class);
        startActivity(intent);

    }

    public void deleteNote(View view) {

        dbHelper.deleteNote(id);
        Intent intent = new Intent(this, ShowNote.class);
        startActivity(intent);

    }

}
