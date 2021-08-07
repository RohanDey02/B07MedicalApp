package com.example.b07medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    static boolean log = false;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        SimpleDateFormat EDTFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        TimeZone EDT = TimeZone.getTimeZone("America/New_York");
        EDTFormat.setTimeZone(EDT);

        // Get current date & time and Date & Time in 7 days
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(EDT);
//        Date currentTime = cal.getTime();
        cal.add(Calendar.DATE, 7);
        Date endOfWeek = cal.getTime();

        // Generate Time Slots :)
        ArrayList<String> timeSlots = new ArrayList<String>();

        cal.add(Calendar.DATE, -7);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date t = cal.getTime();

        while(t.getTime() < endOfWeek.getTime()){
            if(cal.get(Calendar.HOUR_OF_DAY) < 8 && cal.get(Calendar.HOUR_OF_DAY) >= 0){
                cal.add(Calendar.HOUR, 8 - cal.get(Calendar.HOUR_OF_DAY));
            } else if(cal.get(Calendar.HOUR_OF_DAY) >= 16 && cal.get(Calendar.HOUR_OF_DAY) <= 24){
                cal.add(Calendar.HOUR, (24 - cal.get(Calendar.HOUR_OF_DAY)) + 8);
            } else{
                cal.add(Calendar.HOUR, 2);
            }
            t = cal.getTime();
            timeSlots.add(EDTFormat.format(t));
        }

        // Access database
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    Map<String, String> availability = doctor.getAvailability();
                    Map<String, String> allDoctorPastAppointments = doctor.getAllPastAppointments();
                    ArrayList<String> wrongTimeZone = new ArrayList<String>();
                    ArrayList<String> toBeRemoved = new ArrayList<String>();
                    Date d = new Date();

                    // Go through availability and check if there are any old dates.
                    for(Map.Entry<String, String> entry: availability.entrySet()) {
                        // Convert entry key to Date (or Long)
                        try{
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                            sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                            d = sdf.parse(entry.getKey());
                        } catch (ParseException ex){
                            Log.i("ParseException", "Error converting string to date (MUST BE EDT)");
                            wrongTimeZone.add(entry.getKey());
                        } finally {
                            // If Date is older, remove it.
                            if (d.getTime() < System.currentTimeMillis()) {
                                toBeRemoved.add(entry.getKey());
                            }
                        }
                    }

                    if(wrongTimeZone.isEmpty() == false){
                        for(String key: wrongTimeZone){
                            availability.remove(key);
                        }
                    }

                    wrongTimeZone.clear();

                    for(String key: toBeRemoved){
                        if(availability.get(key) != ""){
                            DatabaseReference patientRef = FirebaseDatabase.getInstance(
                                    "https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("patients");
                            ValueEventListener patientListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Patient patient = child.getValue(Patient.class);
                                        if(patient.username == availability.get(key)){
                                            Map<String, String> allPastAppointments = patient.allPastAppointments;
                                            allPastAppointments.put(key, doctor.username);
                                            allDoctorPastAppointments.put(key, patient.username);
                                            patientRef.child(patient.username).child("allPastAppointments").setValue(allPastAppointments);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("warning", "loadPost:onCancelled", databaseError.toException());
                                }
                            };
                            patientRef.addValueEventListener(patientListener);
                        }
                        availability.remove(key);
                    }

                    toBeRemoved.clear();

//                    Log.i("Available Dates", availableDates.toString());

                    // Add all timeSlots to Map
                    for(String timeSlot: timeSlots){
                        if (availability.containsKey(timeSlot) == false && timeSlot.contains("EDT")) {
                            availability.put(timeSlot, "");
                        }
                    }

                    Log.i(doctor.username, doctor.getAvailability().toString());

                    // Add the new availability map
                    if(doctor.username != null) {
                        ref.child(doctor.username).child("availability").setValue(availability);
                        ref.child(doctor.username).child("allPastAppointments").setValue(allDoctorPastAppointments);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(listener);

        SharedPreferences preferences = getSharedPreferences("current_user_info", 0);
        preferences.edit().clear().apply();
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

    // This method will be called on clicking the Register button on the main page.
    public void mainRegister(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //Used when we re-enter the login screen can happen when user signs out or when user presses back button too many times.
    @Override
    protected void onResume() {
        super.onResume();
        log = false;
        SharedPreferences preferences = getSharedPreferences("current_user_info", 0);
        preferences.edit().clear().apply();

    }

    public void login(View view) {
        //gets the text typed in the username text editor
        EditText username = (EditText) findViewById(R.id.editTextTextPersonName4);
        String user_id = username.getText().toString();
        //gets the text typed in the password text editor
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);
        String user_pass = password.getText().toString();

        //Initializing shared preference and setting up an editor
        SharedPreferences p = getSharedPreferences("current_user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();

        //Query the database to find a match for the doctors and the username
        FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Log.i("info", "second");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            //if there is a match then create 4 key value pairs of shared preferences to be used later
                            if(doctor.getUsername() != null && doctor.getPassword() != null && user_id != null && user_pass != null) {
                                if (doctor.getUsername().equals(user_id) && doctor.getPassword().equals(user_pass)) {
                                    editor.putString("username", doctor.getUsername()).apply();
                                    editor.putString("firstName", doctor.getDoctorFirstName()).apply();
                                    editor.putString("lastName", doctor.getDoctorLastName()).apply();
                                    editor.putBoolean("isDoctor", true).apply();

                                    //turn log to true
                                    log = true;
                                    //call nav to go to next activity
                                    success();
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
        //If the username does not match a doctor search through the patients
        FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("patients")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Log.i("info", "first");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Patient patient = snapshot.getValue(Patient.class);
                            if (patient.getUsername().equals(user_id) && patient.getPassword().equals(user_pass)) {
                                editor.putString("username", patient.getUsername()).apply();
                                editor.putString("firstName", patient.getPatientFirstName()).apply();
                                editor.putString("lastName", patient.getPatientLastName()).apply();
                                editor.putBoolean("isDoctor", false).apply();
                                Log.i("info", "logged in");
                                //log = true;
                                success();
                                return;
                            }
                        }
                        if(log == false){
                            fail(view);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    //new method is created because database querying does not execute before other code in the method
    public void success() {
        //Log.i("info", "is called");
        //if log is true then a match was found, send to next activity
        SharedPreferences p = getSharedPreferences("current_user_info", 0);
        boolean isDoctor = p.getBoolean("isDoctor", false);
        Intent intent;
        if (isDoctor) {
            intent = new Intent(this, ListAvailability.class);
        }
        else {
            intent = new Intent(this, ListAppointment.class);
        }
        startActivity(intent);

    }
    public void fail(View view) {
        //Snackbar is used to create a pop-up message
        //Displays pop-up message indicating password or username is incorrect
        Snackbar snackbar = Snackbar.make(view, R.string.invalid_message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        Log.i("info", "username or password incorrect please re-enter");
    }

}