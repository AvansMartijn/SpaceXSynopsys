package com.example.spacexsynopsis;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Launch {
    private String name;
    private String date;
    private Bitmap missionPatch;
    private String rocketName;
    private String launchSiteName;
    private String launchDetails;

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

    public void setLaunchSiteName(String launchSiteName) {
        this.launchSiteName = launchSiteName;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getRocketName() {
        return rocketName;
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

    public Bitmap getMissionPatch() {
        return missionPatch;
    }
}
