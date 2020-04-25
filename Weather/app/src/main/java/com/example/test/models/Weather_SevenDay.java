package com.example.test.models;

public class Weather_SevenDay {

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage() {
        return Image;
    }

    public String getDay() {
        return Day;
    }

    public Weather_SevenDay(String day, String status, String image, String max, String min) {
        Day = day;
        Status = status;
        Image = image;
        this.max = max;
        this.min = min;
    }

    public void setDay(String day) {
        Day = day;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    private String Day;
    private String Status;
    private String Image;
    private String max;
    private String Sunrise;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String country;

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

    public Weather_SevenDay(String country, String day, String status, String image, String max, String feels_like, String doam, String gio, String may, String sunrise, String sunset) {
        Day = day;
        this.country = country;
        Status = status;
        this.feels_like = feels_like;
        Image = image;
        this.max = max;
        this.Sunrise = sunrise;
        this.Sunset = sunset;
        this.doam = doam;
        this.gio = gio;
        this.may = may;
    }

    private String feels_like;
    private String Sunset;

    public String getSunrise() {
        return Sunrise;
    }

    public void setSunrise(String sunrise) {
        Sunrise = sunrise;
    }

    public String getSunset() {
        return Sunset;
    }

    public void setSunset(String sunset) {
        Sunset = sunset;
    }

    public String getDoam() {
        return doam;
    }

    public void setDoam(String doam) {
        this.doam = doam;
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public String getMay() {
        return may;
    }

    public void setMay(String may) {
        this.may = may;
    }

    private String min;
    private String doam;
    private String gio;
    private String may;

    public Weather_SevenDay(String day, String status, String image, String max, String doam, String gio, String may) {
        Day = day;
        Status = status;
        Image = image;
        this.max = max;
        this.doam = doam;
        this.gio = gio;
        this.may = may;
    }
}
