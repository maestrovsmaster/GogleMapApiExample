package com.maestromaster.foursquaremapexample.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    String id="";

    @SerializedName("name")
    @Expose
    String name="";

    @SerializedName("pluralName")
    @Expose
    String pluralName="";

    @SerializedName("shortName")
    @Expose
    String shortName="";

    @SerializedName("icon")
    @Expose
    Icon icon=null;

    @SerializedName("primary")
    @Expose
    boolean primary=true;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public String getShortName() {
        return shortName;
    }

    public Icon getIcon() {
        return icon;
    }

    public boolean isPrimary() {
        return primary;
    }
}
