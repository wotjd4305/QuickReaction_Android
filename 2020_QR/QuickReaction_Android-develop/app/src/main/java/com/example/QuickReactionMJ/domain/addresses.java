package com.example.QuickReactionMJ.domain;

import java.util.List;

public class addresses {

    private String roadAddress;
    private String jibunAddress;
    private String englishAddress;
    private List<AddressElements> addressElements;
    private String x; // 경도
    private String y; // 위도
    private double distance;


    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public void setJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public String getEnglishAddress() {
        return englishAddress;
    }

    public void setEnglishAddress(String englishAddress) {
        this.englishAddress = englishAddress;
    }

    public List<AddressElements> getAddressElements() {
        return addressElements;
    }

    public void setAddressElements(List<AddressElements> addressElements) {
        this.addressElements = addressElements;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "AddressNaver{" +
                "roadAddress='" + roadAddress + '\'' +
                ", jibunAddress='" + jibunAddress + '\'' +
                ", englishAddress='" + englishAddress + '\'' +
                ", addressElements=" + addressElements +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", distance=" + distance +
                '}';
    }

    public addresses(String roadAddress, String jibunAddress, String englishAddress, List<AddressElements> addressElements, String x, String y, double distance) {
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.englishAddress = englishAddress;
        this.addressElements = addressElements;
        this.x = x;
        this.y = y;
        this.distance = distance;
    }
}


