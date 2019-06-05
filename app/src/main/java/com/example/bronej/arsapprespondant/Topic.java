package com.example.bronej.arsapprespondant;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bronej on 5/1/19.
 */

public class Topic implements Parcelable {

    @SerializedName("topic_name")
    private String body;

    @SerializedName("session")
    private String session;

    @Nullable
    @SerializedName("qr_link_name")
    private String qr_link_name;

    @Nullable
    @SerializedName("presentation_name")
    private String presentation_name;

    @SerializedName("id")
    private Integer id;


    public Topic(String body, String session) {
        this.body = body;
        this.session = session;
    }

    protected Topic(Parcel in) {
        body = in.readString();
        session = in.readString();
        qr_link_name = in.readString();
        presentation_name = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Nullable
    public String getQr_link_name() {
        return qr_link_name;
    }

    public void setQr_link_name(@Nullable String qr_link_name) {
        this.qr_link_name = qr_link_name;
    }

    @Nullable
    public String getPresentation_name() {
        return presentation_name;
    }

    public void setPresentation_name(@Nullable String presentation_name) {
        this.presentation_name = presentation_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
        parcel.writeString(session);
        parcel.writeString(qr_link_name);
        parcel.writeString(presentation_name);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
    }
}
