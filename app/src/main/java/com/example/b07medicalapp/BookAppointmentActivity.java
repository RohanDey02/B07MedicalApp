package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        Intent intent = getIntent();

        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int[] textField = {R.id.bk1, R.id.bk2, R.id.bk3, R.id.bk4, R.id.bk5,
                        R.id.bk6, R.id.bk7, R.id.bk8, R.id.bk9, R.id.bk10};
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor doctor = child.getValue(Doctor.class);
                    Log.i("doc info:", doctor.toString());

                    TextView textView = (TextView) findViewById(textField[i]);
                    i++;
                    textView.append(doctor.firstName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(listener);
    }
}