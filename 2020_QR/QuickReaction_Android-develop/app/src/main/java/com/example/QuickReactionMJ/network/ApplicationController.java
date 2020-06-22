package com.example.QuickReactionMJ.network;


import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {
    private String baseUrl;

    private static ApplicationController instance = new ApplicationController();
    public static ApplicationController getInstance() {return instance;}
    private NetworkService networkService;

    @Override
    public void onCreate(){
        super.onCreate();
        buildNetworkService();
        ApplicationController.instance = this;
    }

    public ApplicationController(){
        buildNetworkService();
        ApplicationController.instance = this;
    }


    public NetworkService getNetworkService() {return networkService;}

    public void buildNetworkService(){
        synchronized (ApplicationController.class){
            if(networkService == null){
                Log.i("App","전");
                //기본게이트웨이
                baseUrl = "http://192.168.0.32:8080/"; //원하는 URL 작성
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .create();
                GsonConverterFactory factory = GsonConverterFactory.create(gson);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory)
                        .build();
                networkService = retrofit.create(NetworkService.class);
                Log.i("App","후");
            }
        }
    }
}