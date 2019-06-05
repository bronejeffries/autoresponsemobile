package com.example.bronej.arsapprespondant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bronej on 2/28/19.
 */

public class Option implements Parcelable {

    @SerializedName("id")
    private String option_ID;

    @SerializedName("option_type")
    private String option_type;

    @SerializedName("option")
    private String option_body;


    protected Option(Parcel in) {
        option_ID = in.readString();
        option_type = in.readString();
        option_body = in.readString();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public String getOption_ID() {
        return option_ID;
    }

    public String getOption_type() {
        return option_type;
    }

    public String getOption_body() {
        return option_body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(option_ID);
        parcel.writeString(option_type);
        parcel.writeString(option_body);
    }
}
