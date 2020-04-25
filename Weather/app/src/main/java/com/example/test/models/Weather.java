package com.example.test.models;

public class Weather {
    private String Hours;

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage() {
        return Image;
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

    private String Status;
    private String Image;
    private String max;
    private String min;

    public Weather(String hours, String status, String image, String max, String min) {
        Hours = hours;
        Status = status;
        Image = image;
        this.max = max;
        this.min = min;
    }
}
