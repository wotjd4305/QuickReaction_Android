package com.example.QuickReactionMJ.domain;


import java.util.Arrays;

public class types {
    private String types;

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Type{" +
                "types=" + types +
                '}';
    }

    public types(String types) {
        this.types = types;
    }
}
