package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class _MainResponce {

    @SerializedName("meta")
    @Expose
    Meta meta = null;

    @SerializedName("response")
    @Expose
    Responce response = null;


    public Meta getMeta() {
        return meta;
    }

    public Responce getResponse() {
        return response;
    }
}
