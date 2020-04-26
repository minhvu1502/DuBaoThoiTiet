package com.example.test.models;

public class City {
    private int ID;
    private String City_Name;

    public int getID() {
        return ID;
    }

    public City() {
    }

    public City(int ID, String city_Name, String lat, String lng) {
        this.ID = ID;
        City_Name = city_Name;
        Lat = lat;
        Lng = lng;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    private String Lat;
    private String Lng;
}
