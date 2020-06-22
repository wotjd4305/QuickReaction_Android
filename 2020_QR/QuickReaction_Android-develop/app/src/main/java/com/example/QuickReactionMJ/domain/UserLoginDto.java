package com.example.QuickReactionMJ.domain;

public class UserLoginDto {
    String email;
    String password;
    String duid;

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "duid='" + duid + '\'' +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserLoginDto(String duid, String email, String password) {
        this.duid = duid;
        this.email = email;
        this.password = password;
    }
}
