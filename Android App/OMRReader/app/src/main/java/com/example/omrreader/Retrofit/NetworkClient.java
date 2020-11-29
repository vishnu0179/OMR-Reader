package com.example.omrreader.Retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    private static String baseUrl="https://whispering-fortress-66698.herokuapp.com/";
    private static Retrofit retrofit;
    static Gson gson=new GsonBuilder().setLenient().create();

    public static Retrofit getRetrofit(Context context){
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .build();

        if(retrofit==null){
            retrofit=new Retrofit.Builder().
                    baseUrl(baseUrl).
                    client(okHttpClient).
                    addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
