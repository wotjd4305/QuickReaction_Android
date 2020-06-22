package com.example.QuickReactionMJ.domain;


public class User {

    private String contact;
    private String email;
    private String name;
    private String password;

    public User(String contact, String email, String name, String password) {
        this.contact = contact;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "contact=" + contact +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}