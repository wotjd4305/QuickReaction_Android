package com.example.QuickReactionMJ.domain;

public class SpotAdminJoinDto {

    private String businessNumber;
    private String contact;
    private String email;
    private String name;
    private String password;

    public SpotAdminJoinDto(String businessNumber, String contact, String email, String name, String password) {
        this.businessNumber = businessNumber;
        this.contact = contact;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "SpotAdminJoinDto{" +
                "businessNumber='" + businessNumber + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
