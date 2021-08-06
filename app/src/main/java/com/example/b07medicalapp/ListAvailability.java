package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ListAvailability extends AppCompatActivity {
    private HashMap<String, String> appointments;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_availability);


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
        Log.i("test", appointments.toString());
        String[] array = new String[appointments.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : appointments.entrySet()) {
            array[i] = entry.getKey() + ", " + ((entry.getValue().equals("")) ? "None" : entry.getValue());
            i++;
        }
        // RecyclerView code here
        recyclerView = findViewById(R.id.availabilityrecyclerview);
        MyAdapter myAdapter = new MyAdapter(this, array);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void schedule(View view) {
        Intent intent = new Intent(this, ListSchedule.class);
        startActivity(intent);
    }
}