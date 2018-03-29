package com.example.user.lvndb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.lvndb.R;
import com.example.user.lvndb.db.DBExternal;

import java.util.ArrayList;

public class FindDoctor extends AppCompatActivity {
    Spinner spSpeciality, spDistrict, spArea;
    Button btnFind;
    ArrayList<String> speciality, district, area;
    String selectedDistrict,selectedSpeciality,selectedArea;
    DBExternal dbExternal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        spArea = findViewById(R.id.spArea);
        spDistrict = findViewById(R.id.spDistrict);
        spSpeciality = findViewById(R.id.spSpeciality);
        btnFind = findViewById(R.id.btnSearchDoctor);

        dbExternal = new DBExternal(getApplicationContext());
       //speciality adapter
        speciality = dbExternal.getSpeciality();
        Log.d("Speciality", "Check speciality: "+speciality.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.spText, speciality);
        spSpeciality.setAdapter(adapter);
        //district adapter
        district = dbExternal.getDistrict();
        Log.d("District", "Check district length: "+district.get(0));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.spText, district);
        spDistrict.setAdapter(adapter2);

        selectedDistrict=district.get(0);

        spSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpeciality=spSpeciality.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = spDistrict.getSelectedItem().toString();

                //area adapter
                area = dbExternal.getArea(selectedDistrict);
                //Log.d("Area", "Check area length: "+area.get(0));
                if (!area.isEmpty()) {
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(FindDoctor.this, R.layout.spinner_layout, R.id.spText, area);
                    spArea.setAdapter(adapter3);
                }
                else {
                    String[] aValue={"Not Found"};
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(FindDoctor.this, R.layout.spinner_layout, R.id.spText, aValue);
                    spArea.setAdapter(adapter3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedArea=spArea.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void searchDoctor(View view){
        Intent intent=new Intent(this,ShowDoctor.class);
        intent.putExtra("speciality",selectedSpeciality);
        intent.putExtra("district",selectedDistrict);
        intent.putExtra("location",selectedArea);
        startActivity(intent);
    }
}
