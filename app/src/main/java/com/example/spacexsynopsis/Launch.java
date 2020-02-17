package com.example.spacexsynopsis;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Launch {
    public String name;
    public String date;
    public Bitmap missionPatch;
    private String rocketName;
    private ArrayList<Payload> payloads;
    private String launchSiteName;
    private String launchDetails;

    public Launch(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public Launch() {

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRocketName(String rocketName) {
        this.rocketName = rocketName;
    }

    public void setMissionPatch(Bitmap missionPatch) {
        this.missionPatch = missionPatch;
    }

    public void setPayloads(ArrayList<Payload> payloads) {
        this.payloads = payloads;
    }

    public void setLaunchSiteName(String launchSiteName) {
        this.launchSiteName = launchSiteName;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getMissionPatch() {
        return missionPatch;
    }

    public String getRocketName() {
        return rocketName;
    }

    public ArrayList<Payload> getPayloads() {
        return payloads;
    }

    public String getLaunchSiteName() {
        return launchSiteName;
    }

    public String getLaunchDetails() {
        return launchDetails;
    }

    public void setLaunchDetails(String launchDetails) {
        this.launchDetails = launchDetails;
    }
}
