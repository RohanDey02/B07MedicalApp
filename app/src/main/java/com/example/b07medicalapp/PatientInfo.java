package com.example.b07medicalapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        getData();
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("patients");
        Log.i("username", username);
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("test", "reading");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Patient patient = snapshot.getValue(Patient.class);
                    Log.i("test", patient.toString());
                    if (patient.getUsername().equals(username)) {
                        pCurr = patient;
                        prevDoctors = new ArrayList<>(patient.getAllPastAppointments().values()) ;
                        Log.i("test", prevDoctors.toString());
                        break;
                    }
                }
                ((TextView) findViewById(R.id.textView10)).setText(pCurr.getPatientLastName());
                Log.i("lastname", pCurr.getPatientLastName());
                ((TextView) findViewById(R.id.textView11)).setText(pCurr.getPatientFirstName());
                ((TextView) findViewById(R.id.textView12)).setText(pCurr.getGender().equals("f") ? "Female" : "Male");
//                TextView gender = (TextView) findViewById(R.id.textView12);
                setRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addValueEventListener(listener);

//        ((TextView) findViewById(R.id.textView10)).setText(pCurr.getPatientLastName());
//        Log.i("lastname", pCurr.getPatientLastName());
//        ((TextView) findViewById(R.id.textView11)).setText(pCurr.getPatientFirstName());
//        ((TextView) findViewById(R.id.textView12)).setText(pCurr.getGender());
    }

    public void setRecyclerView() {
//        ArrayList<String> patientsToShow = new ArrayList<String>();
//        for (String p: prevDoctors) {
//            if (!(p.equals(""))) {
//                patientsToShow.add(p);
//            }
//        }
//        String[] array = new String[patientsToShow.size()];
//        int i = 0;
//        for (String p : patientsToShow) {
//            array[i] = p.toString();
//            i++;
//        }
        String array[] = new String[prevDoctors.size()];
        array = prevDoctors.toArray(array);
        // RecyclerView code
        recyclerView = findViewById(R.id.doctorsrecyclerView);
        MyAdapter adapter = new MyAdapter(this, array);
        recyclerView.setAdapter(adapter);
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

    private void getData() {
        if ((getIntent().hasExtra("username")) && !(getIntent().getStringExtra("username").equals("None"))) {
            username = getIntent().getStringExtra("username");
        } else {
            Toast.makeText(this, "Not a patient", Toast.LENGTH_SHORT).show();
        }
    }
}
