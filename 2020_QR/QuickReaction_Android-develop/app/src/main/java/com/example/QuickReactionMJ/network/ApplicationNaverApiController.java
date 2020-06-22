package com.example.QuickReactionMJ.network;


import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationNaverApiController extends Application {
    private String baseUrl;

    private static ApplicationNaverApiController instance = new ApplicationNaverApiController();
    public static ApplicationNaverApiController getInstance() {return instance;}
    private NetworkService networkService;

    @Override
    public void onCreate(){
        super.onCreate();
        buildNetworkService();
        ApplicationNaverApiController.instance = this;
    }

    public ApplicationNaverApiController(){
        buildNetworkService();
        ApplicationNaverApiController.instance = this;
    }


    public NetworkService getNetworkService() {return networkService;}

    public void buildNetworkService(){
        synchronized (ApplicationNaverApiController.class){
            if(networkService == null){
                Log.i("App2 ","전");
                //기본게이트웨이
                baseUrl = "https://naveropenapi.apigw.ntruss.com"; //원하는 URL 작성
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .create();
                GsonConverterFactory factory = GsonConverterFactory.create(gson);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory)
                        .build();
                networkService = retrofit.create(NetworkService.class);
                Log.i("App2","후");
            }
        }
    }
}