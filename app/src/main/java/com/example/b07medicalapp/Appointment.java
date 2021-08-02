package com.example.b07medicalapp;

import java.util.Date;

public class Appointment {
    Patient patient;
    Doctor doctor;
    Date timeSlot;

    public Appointment(Patient patient, Doctor doctor, Date timeSlot){
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
    }
}
