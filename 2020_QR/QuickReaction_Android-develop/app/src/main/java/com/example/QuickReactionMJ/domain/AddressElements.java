package com.example.QuickReactionMJ.domain;

import java.util.Arrays;

public class AddressElements {

    private String[] types;
    private String longName;
    private String shortName;
    private String code;

    public AddressElements(String[] types, String longName, String shortName, String code) {
        this.types = types;
        this.longName = longName;
        this.shortName = shortName;
        this.code = code;
    }

    public AddressElements() {
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "AddressElements{" +
                "types=" + Arrays.toString(types) +
                ", longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}


