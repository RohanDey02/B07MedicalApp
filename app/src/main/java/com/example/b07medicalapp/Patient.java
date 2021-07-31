package com.example.b07medicalapp;

import java.util.ArrayList;
import java.util.Date;

public class Patient extends Account {
    public Patient(){
    }

    public Patient(String username, String password, String firstName, String lastName){
        super(username, password, firstName, lastName);
    }

    public void bookAppointment(Doctor doctor, String timeslot) {
        doctor.availability.put(timeslot, super.username);
    }

    @Override
    public String toString(){
        return "Patient{" +
                "username=" + super.username +
                "first_name=" + super.firstName +
                "last_name=" + super.lastName + '\'' +
                "}";
    }

    public String getUsername() {
        return super.username;
    }

    public void setUsername(String username) {
        super.username = username;
    }

    public String getPassword() {
        return super.password;
    }

    public void setPassword(String password) {
        super.password = password;
    }

    public String getPatientFirstName() {
        return super.firstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        super.firstName = patientFirstName;
    }

    public String getPatientLastName() {
        return super.lastName;
    }

    public void setPatientLastName(String patientLastName) {
        super.lastName = patientLastName;
    }
}
