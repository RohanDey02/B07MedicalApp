package com.example.b07medicalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ListSchedule extends AppCompatActivity {
    private HashMap<String, String> appointments;

    RecyclerView recyclerView;

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
                setRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addValueEventListener(listener);
    }

    public void setRecyclerView() {
        HashMap<String, String> schedule = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : appointments.entrySet()) {
            if (!(entry.getValue().equals(""))) {
                schedule.put(entry.getKey(), entry.getValue());
            }
        }
        String[] array = new String[schedule.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : schedule.entrySet()) {
            array[i] = entry.getKey() + ", " + entry.getValue();
            i++;
        }
        // RecyclerView code
        recyclerView = findViewById(R.id.schedulerecyclerview);
        MyAdapter myAdapter = new MyAdapter(this, array);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void availability(View view) {
        Intent intent = new Intent(this, ListAvailability.class);
        startActivity(intent);
    }

    public void patientInfo(View view){
        Intent intent = new Intent(this, InspectPatients.class);
        startActivity(intent);
    }
}