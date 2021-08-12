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

public class Presenter {
    private MainActivity view;

    private Model model;

    static boolean log = false;

    public Presenter(MainActivity view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void determiner(boolean a, View v) {
        if(a == true) {
            view.success();
        }
        else {
            view.fail(v);
        }
    }
    /*
    public void callSuccess(){
        view.success();
    }

    public void callFail(View v){
        view.fail(v);
    }

     */

    public void getData(View view2, String user_id, String user_pass) {
        model.queryDoctor(view, user_id, user_pass, this, view2);
        model.queryPatient(view2, user_id, user_pass, view, this);
    }
}