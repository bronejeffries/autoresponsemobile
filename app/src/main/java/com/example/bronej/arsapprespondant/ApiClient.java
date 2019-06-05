package com.example.bronej.arsapprespondant;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bronej on 3/5/19.
 */

public class ApiClient {

    public static String BASE_URL;
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(Context context){
        retrofit = null;
        if (retrofit==null){
            BASE_URL = context.getResources().getString(R.string.BASE_URL);
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getApiClient(String url){
        retrofit=null;
        if (retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
