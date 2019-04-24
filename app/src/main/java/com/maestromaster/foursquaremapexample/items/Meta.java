package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Meta {

    @SerializedName("code")
    @Expose
    int code = 0;

    @SerializedName("requestId")
    @Expose
    String requestId = null;

    public int getCode() {
        return code;
    }

    public String getRequestId() {
        return requestId;
    }
}
