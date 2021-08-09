package com.example.b07medicalapp;

public abstract class Account {
    String username;
    String password;
    String firstName;
    String lastName;
    String gender;
    String dateOfBirth;

    public Account() {
    }

    public Account(String username, String password, String firstName, String lastName, String gender, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

}
