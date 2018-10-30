package com.example.elab_yang.egometer.model;

public class Blood {
    private String date;
    private String bloodsugar;

    public Blood(String date, String bloodsugar) {
        this.date = date;
        this.bloodsugar = bloodsugar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBloodsugar() {
        return bloodsugar;
    }

    public void setBloodsugar(String bloodsugar) {
        this.bloodsugar = bloodsugar;
    }
}