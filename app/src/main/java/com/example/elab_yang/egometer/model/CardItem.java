package com.example.elab_yang.egometer.model;

public class CardItem {
    private String date;
    private String time;
    private String speed;
    private String distance;
    private String bpm;
    private String kcal;
    private String before_bloodsugar;
    private String after_bloodsugar;
    private String num;
    private String memo;

    public CardItem(String date, String time, String speed, String distance, String bpm, String kcal, String before_bloodsugar, String after_bloodsugar, String num, String memo) {
        this.date = date;
        this.time = time;
        this.speed = speed;
        this.distance = distance;
        this.bpm = bpm;
        this.kcal = kcal;
        this.before_bloodsugar = before_bloodsugar;
        this.after_bloodsugar = after_bloodsugar;
        this.num = num;
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getBefore_bloodsugar() {
        return before_bloodsugar;
    }

    public void setBefore_bloodsugar(String before_bloodsugar) {
        this.before_bloodsugar = before_bloodsugar;
    }

    public String getAfter_bloodsugar() {
        return after_bloodsugar;
    }

    public void setAfter_bloodsugar(String after_bloodsugar) {
        this.after_bloodsugar = after_bloodsugar;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}