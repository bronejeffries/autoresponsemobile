package com.example.bronej.arsapprespondant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by bronej on 3/5/19.
 */

public interface QuestionApiInterface {

    @GET("{session_key}/questions/{question_id}/")
    Call<Question> getQuestion(@Path("session_key") String session_key,@Path("question_id") String question_id);

    @GET("{session_key}/topic/{topic_id}/")
    Call<Topic> getTopic(@Path("session_key") String session_key,@Path("topic_id") String topic_id);


}
