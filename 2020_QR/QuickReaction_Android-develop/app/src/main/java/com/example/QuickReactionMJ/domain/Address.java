package com.example.QuickReactionMJ.domain;

public class Address {

    private String city;
    private String gunGu;
    private String zipcode;
    private String detail;

    protected Address(){

    }

    public Address(String city, String gunGu, String zipcode, String detail) {
        this.city = city;
        this.gunGu = gunGu;
        this.zipcode = zipcode;
        this.detail = detail;
    }


    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", gunGu='" + gunGu + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}


