package com.example.b07medicalapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private View decorView;
    //Available times for each doctor.
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

        //Doctor names and availability spinner assignment
        Spinner avaSp = (Spinner) findViewById(R.id.availabilitySpinner);
        Spinner docSp = (Spinner) findViewById(R.id.doctorSpinner);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> doctorNames = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    //Get the Doctor object
                    Doctor doctor = child.getValue(Doctor.class);
                    Log.i("doc info:", doctor.toString());

                    //Check the selected item on the doctor names spinner
                    docSp.setOnItemSelectedListener(BookAppointmentActivity.this);
                    //Add the first name and last name initial to the doctor names array list
                    doctorNames.add(doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName().charAt(0) + ".");

                    //Create an arraylist of the availability of the current doctor
                    ArrayList<String> docTimes = new ArrayList<String>();
                    //Get availability of doctor
                    Map<String, String> availability = ((Doctor)doctor).getAvailability();

                    //Loop through the available times for the doctor
                    for (Map.Entry<String, String> entry : availability.entrySet()) {
                        //Check if doctor is not booked and add to the array list
                        if((entry.getValue()).isEmpty()) {
                            docTimes.add(entry.getKey());
                        }
                    }

                    //Add the array list to available times array list
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
        Log.i("info", "clicked");
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

    //Method for the button of filtering
    public void onClick(View view) {
        //Checkbox assignment
        CheckBox male = findViewById(R.id.genderCheckBoxM);
        CheckBox female = findViewById(R.id.genderCheckBoxF);
        //Boolean assignment for checkbox
        Boolean isMale = male.isChecked();
        Boolean isFemale = female.isChecked();

        //Check if both checkboxes are ticked
        if(isMale == true && isFemale == true) {
            //Displays warning message if both checkboxes are ticked
            Snackbar snackbar = Snackbar.make(view, "Can't select both genders", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        //Creates new availableTimes nested arraylist
        availableTimes = new ArrayList<ArrayList<String>>();

        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");

        Spinner avaSp = (Spinner) findViewById(R.id.availabilitySpinner);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> doctorNames = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor doctor = child.getValue(Doctor.class);
                    Log.i("doc info:", doctor.toString());
                    Spinner docSp = (Spinner) findViewById(R.id.doctorSpinner);

                    //docSp.setOnItemSelectedListener(BookAppointmentActivity.this);

                    //If doctor is male and male is selected then proceed to create spinners
                    if(isMale == true && isFemale == false && doctor.getGender().equals("m")) {
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
                    //if doctor is female and female is selected than proceed to create spinners
                    else if(isFemale == true && isMale == false && doctor.getGender().equals("f")) {
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(listener);
        //Log.i("info", "Are you male?" + isMale);
        //Log.i("info", "Are you female?" + isFemale);
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

        //Query's through the database and modifies the hashmap of doctor with docName to add the patient username patientUser to the schedule at time timeSlot
        FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Log.i("info", "second");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            //Stores the current doctor name in curName
                            String curName = doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName().charAt(0) + ".";

                            //If curName is the same as the docName of the spinner then loop through the hashmap of current doctor
                            if(curName.equals(docName)) {
                                Log.i("info", "this " + curName);

                                Map<String, String> availability = ((Doctor)doctor).getAvailability();
                                //loops through the hashmap keys
                                for (String time : availability.keySet()) {
                                    //if hashmap key is the same as the time of timeSlot then set new value of key to the logged in patient username
                                    if(time.equals(timeSlot)) {
                                        Log.i("info", "here " + time);
                                        Log.i("info", availability.get(time) + "eyebrow");
                                        //Replaces the value of the key time with patientUser
                                        availability.put(time, patientUser);
                                        Log.i("info", availability.get(time));

                                        //Set doctor hashmap to new hashmap
                                        doctor.setAvailability(availability);

                                        //Write the new hashmap to database
                                        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
                                        ref.child(doctor.getUsername()).child("availability").setValue(availability);
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });


        Log.i("info", docName);
        Log.i("info", timeSlot);

        //Refreshes the page when "book appointment" button is clicked
        finish();
        overridePendingTransition(0, 0); //To get rid of the transition time of refreshing
        startActivity(getIntent());
        overridePendingTransition(0, 0); //To get rid of the transition time of refreshing
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