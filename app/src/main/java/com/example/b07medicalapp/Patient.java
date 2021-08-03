package com.example.b07medicalapp;

public class Patient extends Account implements Comparable<Patient>{
    public Patient(){
    }

    public Patient(String username, String password, String firstName, String lastName, String gender){
        super(username, password, firstName, lastName, gender);
    }

    public void bookAppointment(Doctor doctor, String timeslot) {
        doctor.availability.put(timeslot, super.username);
    }

    @Override
    public String toString(){
        return "Patient{" +
                "username=" + super.username +
                "first_name=" + super.firstName +
                "last_name=" + super.lastName +
                "gender=" + super.gender + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o){
        Patient p = (Patient) o;
        return (this.getPatientFirstName() == p.getPatientFirstName() &&
                this.getPatientLastName() == p.getPatientLastName() &&
                this.getPassword() == p.getPassword() &&
                this.getUsername() == p.getUsername());
    }

    @Override
    public int hashCode(){
        int a = this.getUsername().hashCode() + this.getPassword().hashCode() +
                this.getPatientFirstName().hashCode() + this.getPatientLastName().hashCode();
        return a;
    }

    @Override
    public int compareTo(Patient p){
        int a1 = this.hashCode();
        int a2 = p.hashCode();
        if(a1>a2)
            return 1;
        else if (a1==a2)
            return 0;
        else
            return -1;
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

    public String getGender() {
        return super.gender;
    }

    public void setPatientGender(String patientGender) {
        super.gender = patientGender;
    }
}
