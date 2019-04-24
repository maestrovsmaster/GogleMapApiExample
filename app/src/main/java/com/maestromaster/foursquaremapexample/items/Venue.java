package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Venue {

    @SerializedName("id")
    @Expose
    String id="";

    @SerializedName("name")
    @Expose
    String name="";

    @SerializedName("location")
    @Expose
    Location location = null;

    @SerializedName("categories")
    @Expose
    ArrayList<Category> categories  = new ArrayList<>();

    @SerializedName("referralId")
    @Expose
    String referralId="";

    @SerializedName("hasPerk")
    @Expose
    boolean hasPerk= false;


    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
