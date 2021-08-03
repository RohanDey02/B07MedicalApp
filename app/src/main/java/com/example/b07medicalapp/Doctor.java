package com.example.b07medicalapp;

import java.util.HashMap;
import java.util.Map;

public class Doctor extends Account {
    String specialization;
    Map<String, String> availability = new HashMap<String, String>();

    public Doctor(){
    }

    public Doctor(String username, String password, String firstName, String lastName, String gender, String specialization){
        super(username, password, firstName, lastName, gender);
        this.specialization = specialization;
    }

    @Override
    public String toString(){
        return "Doctor{" +
                "username=" + super.username +
                "first_name=" + super.firstName +
                "last_name=" + super.lastName +
                "gender=" + super.gender +
                "specialization=" + specialization +
                "time_slots" + availability + '\'' +
                "}";
    }

    // Precondition: Time slot should be of form: YYYY-MM-DD HH:mm AM/PM
    public void addTimeSlot(String timeSlot){
        availability.put(timeSlot, "");
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
    
    public String getDoctorFirstName() {
        return super.firstName;
    }

    public void setDoctorFirstName(String doctorFirstName) {
        super.firstName = doctorFirstName;
    }

    public String getDoctorLastName() {
        return super.lastName;
    }

    public void setDoctorLastName(String doctorLastName) {
        super.lastName = doctorLastName;
    }

    public String getGender() {
        return super.gender;
    }

    public void setDoctorGender(String doctorGender) {
        super.gender = doctorGender;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.specialization = doctorSpecialization;
    }

    public Map<String, String> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, String> availability) {
        this.availability = availability;
    }
}
