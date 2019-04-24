package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabeledLatLng {

    @SerializedName("label")
    @Expose
    String annotation="";

    @SerializedName("lat")
    @Expose
    double lat=0;

    @SerializedName("lng")
    @Expose
    double  lng=0;
}
