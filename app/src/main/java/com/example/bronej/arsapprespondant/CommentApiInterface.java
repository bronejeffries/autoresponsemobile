package com.example.bronej.arsapprespondant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by bronej on 3/5/19.
 */

public interface CommentApiInterface {

    @POST("{session_key}/comments/")
    Call<Comment> createComment(@Path("session_key") String session_key, @Body Comment comment);


}
