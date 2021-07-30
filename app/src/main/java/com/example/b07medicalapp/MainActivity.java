package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
    }

    // This method will be called on clicking the Register button on the main page.
    public void mainRegister(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}