package com.example.b07medicalapp;

import java.util.ArrayList;
import java.util.Date;

public class Patient extends Account {
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    public Patient(){
    }

    public Patient(String username, String firstName, String lastName){
        super(username, firstName, lastName);
    }

    public void bookAppointment(Doctor doctor, Date timeslot) {
        int index = doctor.timeSlots.indexOf(timeslot);

        if(index != -1) {
            doctor.availability.set(index, false);
            appointments.add(new Appointment(this, doctor, timeslot));
        } else{
            // Is this how I'd print this out?
            System.out.println("Cannot Book Appointment!");
        }
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

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }
}
