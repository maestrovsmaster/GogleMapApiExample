package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Location {

    @SerializedName("address")
    @Expose
    String address="";

    @SerializedName("lat")
    @Expose
    double lat=0;

    @SerializedName("lng")
    @Expose
    double lng=0;

    @SerializedName("labeledLatLngs")
    @Expose
    ArrayList<LabeledLatLng>  labeledLatLngs = new ArrayList();

    @SerializedName("distance")
    @Expose
    double distance=0;

    @SerializedName("cc")
    @Expose
    String cc="";

    @SerializedName("city")
    @Expose
    String city="";

    @SerializedName("state")
    @Expose
    String state="";

    @SerializedName("country")
    @Expose
    String country="";

    @SerializedName("formattedAddress")
    @Expose
    ArrayList<String> formattedAddress = new ArrayList<>();


    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public ArrayList<LabeledLatLng> getLabeledLatLngs() {
        return labeledLatLngs;
    }

    public double getDistance() {
        return distance;
    }

    public String getCc() {
        return cc;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<String> getFormattedAddress() {
        return formattedAddress;
    }
}
