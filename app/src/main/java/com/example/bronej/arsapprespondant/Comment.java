package com.example.bronej.arsapprespondant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bronej on 3/5/19.
 */

public class Comment {
    @SerializedName("comment")
    private String comment;

    @SerializedName("topic")
    private String question_id;

    @SerializedName("id")
    private Integer id;

    public Comment(String comment, String question_id) {
        this.comment = comment;
        this.question_id = question_id;
    }

    public String getComment() {
        return comment;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public Integer getId() {
        return id;
    }

}
