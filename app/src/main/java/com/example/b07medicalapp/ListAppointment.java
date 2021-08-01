package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointment);

        Intent intent = getIntent();
    }

    //open the book appointment activity page when 'Book' is pressed
    public void openBookAppointmentPage(View view) {
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        startActivity(intent);
    }
}