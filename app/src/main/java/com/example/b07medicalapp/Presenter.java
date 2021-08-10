package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Presenter extends AppCompatActivity {

    private View view;

    static boolean log = false;

    public Presenter(View view) {
        this.view = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter);
    }

    public void getData(Context context, String user_id, String user_pass) {

        //Log.i("info", user_id);
        //Log.i("info", user_pass);


        //Initializing shared preference and setting up an editor
        SharedPreferences p = context.getSharedPreferences("current_user_info", Context.MODE_PRIVATE);
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
                                    success(context);
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
                                success(context);
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
    public void success(Context context) {
        //Log.i("info", "is called");
        //if log is true then a match was found, send to next activity
        SharedPreferences p = context.getSharedPreferences("current_user_info", 0);
        boolean isDoctor = p.getBoolean("isDoctor", false);
        Intent intent;
        if (isDoctor) {
            intent = new Intent(context, ListAvailability.class);
        }
        else {
            intent = new Intent(context, ListAppointment.class);
        }
        context.startActivity(intent);

    }
    public void fail(View view) {
        //Snackbar is used to create a pop-up message
        //Displays pop-up message indicating password or username is incorrect
        Snackbar snackbar = Snackbar.make(view, R.string.invalid_message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        Log.i("info", "username or password incorrect please re-enter");
    }



}