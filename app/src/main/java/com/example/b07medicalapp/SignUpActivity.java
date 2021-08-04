package com.example.b07medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        Intent intent = new Intent(this, MainActivity.class);

        EditText firstName = findViewById(R.id.editTextFirstName);
        EditText lastName = findViewById(R.id.editTextLastName);
        EditText username = findViewById(R.id.editTextUsername);
        EditText password = findViewById(R.id.editTextPassword);
        EditText specialization = findViewById(R.id.editTextSpecialization);
        ToggleButton genderToggle = findViewById(R.id.toggleButtonGender);
        ToggleButton roleToggle = findViewById(R.id.toggleButtonDoctor);

        Button button = findViewById(R.id.button4);

        final boolean[] doctor = {false};
        final boolean[] female = {false};

        roleToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isDoctor) {
                if (isDoctor) {
                    doctor[0] = true;
                } else {
                    doctor[0] = false;
                }
            }
        });

        genderToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isFemale) {
                if (isFemale) {
                    female[0] = true;
                } else {
                    female[0] = false;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctor[0]) {
                    String gender;
                    if (female[0]) {
                        gender = "f";
                    } else {
                        gender = "m";
                    }

                    Doctor account = new Doctor(
                            username.getText().toString(),
                            password.getText().toString(),
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            gender,
                            specialization.getText().toString());

                    if (account.firstName.isEmpty()) {
                        firstName.setError("first name is empty");
                        firstName.requestFocus();
                        return;
                    }

                    if (account.lastName.isEmpty()) {
                        lastName.setError("last name is empty");
                        lastName.requestFocus();
                        return;
                    }

                    if (account.password.isEmpty()) {
                        password.setError("password is empty");
                        password.requestFocus();
                        return;
                    }

                    if (account.username.isEmpty()) {
                        username.setError("username is empty");
                        username.requestFocus();
                        return;
                    }

                    if (account.specialization.isEmpty()) {
                        specialization.setError("specialization is empty");
                        specialization.requestFocus();
                        return;
                    }

                    DatabaseReference db = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference();
                    db.child("doctors").child(account.username).setValue(account);
                } else { // Patient
                    String gender;
                    if (female[0]) {
                        gender = "f";
                    } else { // Male
                        gender = "m";
                    }

                    Patient account = new Patient(
                            username.getText().toString(),
                            password.getText().toString(),
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            gender);

                    if (account.firstName.isEmpty()) {
                        firstName.setError("first name is empty");
                        firstName.requestFocus();
                        return;
                    }

                    if (account.lastName.isEmpty()) {
                        lastName.setError("last name is empty");
                        lastName.requestFocus();
                        return;
                    }

                    if (account.password.isEmpty()) {
                        password.setError("password is empty");
                        password.requestFocus();
                        return;
                    }

                    if (account.username.isEmpty()) {
                        username.setError("username is empty");
                        username.requestFocus();
                        return;
                    }

                    DatabaseReference db = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference();
                    db.child("patients").child(account.username).setValue(account);
                }
                startActivity(intent);
            }
        });
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
}