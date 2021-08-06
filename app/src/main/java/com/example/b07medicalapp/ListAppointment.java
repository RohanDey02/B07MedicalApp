package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map.Entry;

public class ListAppointment extends AppCompatActivity {
    String username;
    Map<String, String> p_appointments = new HashMap<String, String>();
    boolean passed = false;

    RecyclerView recyclerView;

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_appointment);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        SharedPreferences p = getSharedPreferences("current_user_info", 0);
        username = p.getString("username", "");
        Log.i("patient", username);

        Intent intent = getIntent();

        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("info", "first");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    Log.i("Doctor", doctor.getDoctorFirstName());
                    Map<String, String> availability = doctor.getAvailability();
                    for (Map.Entry<String, String> entry : availability.entrySet()) {
                        String date = entry.getKey();
                        String name = entry.getValue();
                        Log.i("Doctor", name);
                        if (name.equals(username)) {
                            p_appointments.put(date + " " + doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName().charAt(0), "");
                            passed = true;
                        }
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void setRecyclerView() {
        Log.i("appointments", p_appointments.toString());
        Log.i("passed", String.valueOf(passed));
        String[] array = new String[p_appointments.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : p_appointments.entrySet())
        {
            array[i] = entry.getKey() + ", " + entry.getValue();
            i++;
        }
        // Array initialised by this line
        // RecyclerView code
        recyclerView = findViewById(R.id.listrecyclerview);
        MyAdapter myAdapter = new MyAdapter(this, array);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //open the book appointment activity page when 'Book' is pressed
    public void openBookAppointmentPage(View view) {
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        startActivity(intent);
    }
}