package com.example.bronej.arsapprespondant;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bronej on 2/28/19.
 */

public class Question implements Parcelable {

    @SerializedName("question")
    private String body;

    @SerializedName("session")
    private String session;

    @SerializedName("option_type")
    private String option_type;

    @Nullable
    @SerializedName("qr_link_name")
    private String qr_link_name;

    @SerializedName("id")
    private Integer id;

    public Question(String session_id, String body, String option_type) {
        this.body = body;
        this.session = session_id;
        this.option_type = option_type;
    }


    protected Question(Parcel in) {
        body = in.readString();
        session = in.readString();
        option_type = in.readString();
        qr_link_name = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getSession_id() {
        return session;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setQr_link_name(@Nullable String qr_link_name) {
        this.qr_link_name = qr_link_name;
    }

    @Nullable
    public String getQr_link_name() {
        return qr_link_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
        parcel.writeString(session);
        parcel.writeString(option_type);
        parcel.writeString(qr_link_name);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
    }
}
