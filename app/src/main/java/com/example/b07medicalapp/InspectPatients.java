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

import java.util.ArrayList;

public class InspectPatients extends AppCompatActivity {
    private ArrayList<String> patients;

    RecyclerView recyclerView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_patients);

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
                        patients = (ArrayList<String>) doctor.getPatients();
                        Log.i("test", patients.toString());
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
        ArrayList<String> patientsToShow = new ArrayList<String>();
        for (String p: patients) {
            if (!(p.equals(""))) {
                patientsToShow.add(p);
            }
        }
        String[] array = new String[patientsToShow.size()];
        int i = 0;
        for (String p : patientsToShow) {
            array[i] = p.toString();
            i++;
        }
        // RecyclerView code
        recyclerView = findViewById(R.id.patientsrecyclerView);
        PatientsAdapter pAdapter = new PatientsAdapter(this, array);
        recyclerView.setAdapter(pAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void schedule(View view) {
        Intent intent = new Intent(this, ListSchedule.class);
        startActivity(intent);
    }

    public void availability(View view) {
        Intent intent = new Intent(this, ListAvailability.class);
        startActivity(intent);
    }
}
