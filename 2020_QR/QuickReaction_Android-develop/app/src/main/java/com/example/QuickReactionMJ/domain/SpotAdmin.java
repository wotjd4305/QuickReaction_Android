package com.example.QuickReactionMJ.domain;

public class SpotAdmin {

   //private Long id;
    private String name;
    private String businessNumber;
    private String contact;
    //private Spot spot;

    public SpotAdmin(String name, String businessNumber, String contact){
        this.name=name;
        this.businessNumber=businessNumber;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "SpotAdmin{" +
                "name='" + name + '\'' +
                ", businessNumber='" + businessNumber + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
