package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ListSchedule extends AppCompatActivity {
    private HashMap<String, String> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);


        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
        SharedPreferences p = getSharedPreferences("current_user_info", 0);
        String username = p.getString("username", "");
        Log.i("test", username);
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("test", "reading");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    Log.i("test", doctor.toString());
                    if (doctor.getUsername().equals(username)) {
                        appointments = (HashMap<String, String>) doctor.getAvailability();
                        Log.i("test", "none" + appointments.toString());
                        break;
                    }
                }
                setList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addValueEventListener(listener);
    }

    public void setList() {
        HashMap<String, String> schedule = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : appointments.entrySet()) {
            if (!(entry.getValue().equals(""))) {
                schedule.put(entry.getKey(), entry.getValue());
            }
        }
        String[] spinnerArray = new String[schedule.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : schedule.entrySet()) {
            spinnerArray[i] = entry.getKey() + ", " + entry.getValue();
            i++;
        }
        Spinner sp = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListSchedule.this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

    public void availability(View view) {
        Intent intent = new Intent(this, ListAvailability.class);
        startActivity(intent);
    }
}