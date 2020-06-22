package com.example.QuickReactionMJ.domain;

public class SpotAdminLoginDto {

   //private Long id;
    private String duid;
    private String email;
    private String password;
    //private String contact;
    //private Spot spot;


    @Override
    public String toString() {
        return "SpotAdminLoginDto{"  +
                "duid='" + duid + '\'' +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public SpotAdminLoginDto(String duid, String email, String password) {
        this.duid = duid;
        this.email = email;
        this.password = password;
    }
}
