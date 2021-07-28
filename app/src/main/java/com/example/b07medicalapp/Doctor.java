package com.example.b07medicalapp;

import java.util.ArrayList;
import java.util.Date;

public class Doctor extends Account {
    // Fields: Parallel List w/ time slot & availability
    ArrayList<Date> timeSlots = new ArrayList<Date>();
    ArrayList<Boolean> availability = new ArrayList<Boolean>();

    public Doctor(){
    }

    public Doctor(String username, String firstName, String lastName){
        super(username, firstName, lastName);
    }

    @Override
    public String toString(){
        return "Doctor{" +
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

    public String getDoctorFirstName() {
        return super.firstName;
    }

    public void setDoctorFirstName(String patientFirstName) {
        super.firstName = patientFirstName;
    }

    public String getDoctorLastName() {
        return super.lastName;
    }

    public void setDoctorLastName(String patientLastName) {
        super.lastName = patientLastName;
    }

    public ArrayList<Date> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<Date> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public ArrayList<Boolean> getAvailability() {
        return availability;
    }

    public void setAvailability(ArrayList<Boolean> availability) {
        this.availability = availability;
    }
}
