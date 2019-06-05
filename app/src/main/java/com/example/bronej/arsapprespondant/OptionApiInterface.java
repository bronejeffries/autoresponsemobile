package com.example.bronej.arsapprespondant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by bronej on 3/5/19.
 */

public interface OptionApiInterface {

    @GET(" ")
    Call<List<Option>> getOptions();

    @PUT("{session_key}/option/{option_id}/")
    Call<Option> updateoptionchoice(@Path("session_key") String session_key,@Path("option_id") Integer option_id);


}
