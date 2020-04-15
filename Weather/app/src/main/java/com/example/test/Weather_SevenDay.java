package com.example.test;

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
    private String min;

}
