package com.example.b07medicalapp;

public abstract class Account {
    String username;
    String firstName;
    String lastName;

    public Account() {
    }

    public Account(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
