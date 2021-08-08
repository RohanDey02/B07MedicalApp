package com.example.b07medicalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientInfo extends AppCompatActivity {
    private View decorView;
    private ArrayList<String> prevDoctors;
    RecyclerView recyclerView;
    Patient pCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("patients");
        SharedPreferences p = getSharedPreferences("current_user_info", 0);
        String username = p.getString("username", "");
        Log.i("test", username);
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("test", "reading");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Patient patient = snapshot.getValue(Patient.class);
                    Log.i("test", patient.toString());
                    if (patient.getUsername().equals(username)) {
                        pCurr = patient;
                        prevDoctors = new ArrayList<>(patient.getAllAppointments().values()) ;
                        Log.i("test", prevDoctors.toString());
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
        for (String p: prevDoctors) {
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
        recyclerView = findViewById(R.id.doctorsrecyclerView);
        DoctorsAdapter dAdapter = new DoctorsAdapter(this, array);
        recyclerView.setAdapter(dAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void showLastName(View v){
        String msg = pCurr.lastName;
        ((TextView) findViewById(R.id.textView10)).setText(msg);
        Log.d("info", msg);
    }

    public void showFirstName(View v){
        String msg = pCurr.firstName;
        ((TextView) findViewById(R.id.textView11)).setText(msg);
        Log.d("info", msg);
    }

    public void showGender(View v){
        String msg;
        if(pCurr.gender.equals("m"))
            msg = "Male";
        else
            msg = "Female";

        ((TextView) findViewById(R.id.textView12)).setText(msg);
        Log.d("info", msg);
    }
}
