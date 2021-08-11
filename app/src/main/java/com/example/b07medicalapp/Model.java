package com.example.b07medicalapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Model {

    public void queryDoctor(MainActivity view, String user_id, String user_pass, Presenter presenter) {
        //Initializing shared preference and setting up an editor
        SharedPreferences p = view.getSharedPreferences("current_user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();

        //Query the database to find a match for the doctors and the username
        FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                                    Presenter.log = true;
                                    //call nav to go to next activity
                                    presenter.callSuccess();
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
        Log.i("Info", "Doctor " + Presenter.log);
//        return Presenter.log;
    }

    public void queryPatient(View view2, String user_id, String user_pass, MainActivity view, Presenter presenter) {
        //Initializing shared preference and setting up an editor
        SharedPreferences p = view.getSharedPreferences("current_user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
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
                                presenter.callSuccess();
                                return;
                            }
                        }
                        if(Presenter.log == false){
                            presenter.callFail(view2);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Log.i("Info", "Patient " + Presenter.log);
//        return Presenter.log;
    }
}