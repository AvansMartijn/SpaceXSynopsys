package com.example.spacexsynopsis;

import android.graphics.Bitmap;

public class Launch {
    public String name;
    public String date;
    public Bitmap missionPatch;

    public Launch(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public void setMissionPatch(Bitmap missionPatch) {
        this.missionPatch = missionPatch;
    }
}
