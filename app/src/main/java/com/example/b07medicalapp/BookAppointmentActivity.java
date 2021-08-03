package com.example.b07medicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity {
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://b07projectdatabase-default-rtdb.firebaseio.com/").getReference("doctors");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_book_appointment);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int[] textField = {R.id.bk1, R.id.bk2, R.id.bk3, R.id.bk4, R.id.bk5,
                        R.id.bk6, R.id.bk7, R.id.bk8, R.id.bk9, R.id.bk10};

                int[] spinnerField = {R.id.s1, R.id.s2, R.id.s3, R.id.s4, R.id.s5,
                        R.id.s6, R.id.s7, R.id.s8, R.id.s9, R.id.s10};

                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor doctor = child.getValue(Doctor.class);
                    Log.i("doc info:", doctor.toString());

                    TextView textView = (TextView) findViewById(textField[i]);
                    textView.setText(doctor.firstName + " " + doctor.lastName.charAt(0) + ".");

                    Spinner spinner = (Spinner) findViewById(spinnerField[i]);
                    Map<String, String> availability = doctor.getAvailability();
                    ArrayList<String> dates = new ArrayList<>();
                    for (Map.Entry<String, String> entry : availability.entrySet()) {
                        dates.add(entry.getKey());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookAppointmentActivity.this, android.R.layout.simple_spinner_item, dates);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(listener);
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