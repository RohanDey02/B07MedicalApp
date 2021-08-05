package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private View decorView;
    ArrayList<ArrayList<String>> availableTimes = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_book_appointment);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });


        Spinner avaSp = (Spinner) findViewById(R.id.availabilitySpinner);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> doctorNames = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor doctor = child.getValue(Doctor.class);
                    Log.i("doc info:", doctor.toString());
                    Spinner docSp = (Spinner) findViewById(R.id.doctorSpinner);

                    docSp.setOnItemSelectedListener(BookAppointmentActivity.this);
                    doctorNames.add(doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName().charAt(0) + ".");

                    ArrayList<String> docTimes = new ArrayList<String>();
                    Map<String, String> availability = ((Doctor)doctor).getAvailability();
                    for (Map.Entry<String, String> entry : availability.entrySet()) {
                        if((entry.getValue()).isEmpty()) {
                            docTimes.add(entry.getKey());
                        }
                    }
                    availableTimes.add(docTimes);

                    ArrayAdapter<String> docAdapter = new ArrayAdapter<String>(BookAppointmentActivity.this, android.R.layout.simple_spinner_item, doctorNames);
                    docAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    docSp.setAdapter(docAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(listener);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner avaSp = (Spinner) findViewById(R.id.availabilitySpinner);
        ArrayAdapter<String> avaAdapter;
        String item = adapterView.getItemAtPosition(i).toString();

        for(int j = 0; j <= i; j++){
            if(j == i){
                avaAdapter = new ArrayAdapter<String>(BookAppointmentActivity.this, android.R.layout.simple_spinner_item, availableTimes.get(j));
                avaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                avaSp.setAdapter(avaAdapter);
                break;
            }
        }

        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    public void bookAppointment(View view) {
        //Make spinners with id of the spinner of doctor names (docSp) and spinner of their schedules (avaSp)
        Spinner docSp = (Spinner) findViewById(R.id.doctorSpinner);
        Spinner avaSp = (Spinner) findViewById(R.id.availabilitySpinner);

        //Stores the name of doctor from spinner docSp into string docName
        String docName = String.valueOf(docSp.getSelectedItem());
        //Stores the timeslot of the doctor from spinner avaSp into string timeSlot
        String timeSlot = String.valueOf(avaSp.getSelectedItem());

        //Get shared preference to read username of patient
        SharedPreferences p = getSharedPreferences("current_user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        String patientUser = p.getString("username", "");
        Log.i("info", patientUser);

        //Display information of the two strings docName and timeSlot
        //Replace with appointment booking implementation
        Log.i("info", docName);
        Log.i("info", timeSlot);



        //Refreshes the page when "book appointment" button is clicked
        finish();
        startActivity(getIntent());
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
}