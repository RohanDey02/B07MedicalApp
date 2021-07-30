package com.example.b07medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
    }

    // This method will be called on clicking the Register button on the signup page.
    public void signUpRegister(View view) throws InterruptedException {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}
