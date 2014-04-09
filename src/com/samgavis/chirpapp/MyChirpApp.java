package com.samgavis.chirpapp;

import android.content.Context;

public class MyChirpApp extends ChirpAppActivity {

    private static Context context;

    public void onCreate(){
        MyChirpApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyChirpApp.context;
    }
}
