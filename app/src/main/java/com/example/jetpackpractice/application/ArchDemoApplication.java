package com.example.jetpackpractice.application;

import android.app.Application;

import com.example.network.base.NetworkApi;

public class ArchDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkApi.init(new NetworkRequestInfo(this));
    }
}
