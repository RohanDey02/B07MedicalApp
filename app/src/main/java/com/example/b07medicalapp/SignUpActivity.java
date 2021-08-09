package com.example.b07medicalapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.util.Calendar;

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
        Spinner specialization = (Spinner)findViewById(R.id.spinnerSpecialization);
        ArrayAdapter<CharSequence> specialization_adapter = ArrayAdapter.createFromResource(this, R.array.specialization_array, R.layout.spinner_specialization_signup);
        specialization_adapter.setDropDownViewResource(R.layout.spinner_specialization_dropdown_signup);
        specialization.setAdapter(specialization_adapter);
        ToggleButton genderToggle = findViewById(R.id.toggleButtonGender);
        ToggleButton roleToggle = findViewById(R.id.toggleButtonDoctor);
        Button dateOfBirth = findViewById(R.id.buttonDOB);

        Button button = findViewById(R.id.button4);

        final boolean[] doctor = {false};
        final boolean[] female = {false};
        final Calendar[] calendar = new Calendar[1];
        final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];
        final String[] dob = new String[1];

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               calendar[0] = Calendar.getInstance();
               int day = calendar[0].get(Calendar.DAY_OF_MONTH);
               int month = calendar[0].get(Calendar.MONTH);
               int year =  calendar[0].get(Calendar.YEAR);

               datePickerDialog[0] = new DatePickerDialog(SignUpActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                       if(year > Calendar.getInstance().get(Calendar.YEAR)){
                           Snackbar snackbar = Snackbar.make(view, "Invalid Date of Birth!", Snackbar.LENGTH_SHORT);
                           snackbar.show();
                           return;
                       } else if(year == Calendar.getInstance().get(Calendar.YEAR)){
                           if(month > Calendar.getInstance().get(Calendar.MONTH)){
                               Snackbar snackbar = Snackbar.make(view, "Invalid Date of Birth!", Snackbar.LENGTH_SHORT);
                               snackbar.show();
                               return;
                           } else if(month == Calendar.getInstance().get(Calendar.MONTH)){
                               if(day > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                                   Snackbar snackbar = Snackbar.make(view, "Invalid Date of Birth!", Snackbar.LENGTH_SHORT);
                                   snackbar.show();
                                   return;
                               }
                           }
                       } else {
                           if (day < 10) {
                               if (month < 9) {
                                   dob[0] = year + "/0" + (month + 1) + "/0" + day;
                               } else {
                                   dob[0] = year + "/" + (month + 1) + "/0" + day;
                               }
                           } else {
                               if (month < 9) {
                                   dob[0] = year + "/0" + (month + 1) + "/" + day;
                               } else {
                                   dob[0] = year + "/" + (month + 1) + "/" + day;
                               }
                           }
                       }

                       Snackbar snackbar = Snackbar.make(view, dob[0] + " was selected!", Snackbar.LENGTH_SHORT);
                       snackbar.show();
                   }
               }, day, month, year);
               datePickerDialog[0].show();
           }
       });

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
                            specialization.getSelectedItem().toString(),
                            dob[0]);

                    if (account.firstName.isEmpty()) {
                        firstName.setError("First name is empty");
                        firstName.requestFocus();
                        return;
                    }

                    if (account.lastName.isEmpty()) {
                        lastName.setError("Last name is empty");
                        lastName.requestFocus();
                        return;
                    }

                    if (account.password.isEmpty()) {
                        password.setError("Password is empty");
                        password.requestFocus();
                        return;
                    }

                    if (account.username.isEmpty()) {
                        username.setError("Username is empty");
                        username.requestFocus();
                        return;
                    }

                    if (account.specialization.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Specialization is empty!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }

                    if (account.dateOfBirth.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Date of Birth!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
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
                            gender,
                            dob[0]);

                    if (account.firstName.isEmpty()) {
                        firstName.setError("First name is empty");
                        firstName.requestFocus();
                        return;
                    }

                    if (account.lastName.isEmpty()) {
                        lastName.setError("Last name is empty");
                        lastName.requestFocus();
                        return;
                    }

                    if (account.password.isEmpty()) {
                        password.setError("Password is empty");
                        password.requestFocus();
                        return;
                    }

                    if (account.username.isEmpty()) {
                        username.setError("Username is empty");
                        username.requestFocus();
                        return;
                    }

                    if (account.dateOfBirth.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Date of Birth!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
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