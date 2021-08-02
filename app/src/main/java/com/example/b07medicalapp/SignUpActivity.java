package com.example.b07medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();

        EditText etFirstName = findViewById(R.id.editTextFirstName);
        EditText etLastName = findViewById(R.id.editTextLastName);
        EditText etUserName = findViewById(R.id.editTextUsername);
        EditText etPassword = findViewById(R.id.editTextPassword);
        Button button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = new Patient(
                        etUserName.getText().toString(),
                        etPassword.getText().toString(),
                        etFirstName.getText().toString(),
                        etLastName.getText().toString());
                DatabaseReference db = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference();
                db.child("patients").child(account.username).setValue(account);

            }
        });
    }

    // This method will be called on clicking the Register button on the signup page.
    public void signUpRegister(View view) throws InterruptedException {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}
