package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Responce {

    @SerializedName("venues")
    @Expose
    ArrayList<Venue> venues = new ArrayList<>();

    @SerializedName("confident")
    @Expose
    boolean confident = false;

    public ArrayList<Venue> getVenues() {
        return venues;
    }
}
