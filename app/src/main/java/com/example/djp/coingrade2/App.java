package com.example.djp.coingrade2;

import android.app.Application;
import android.content.Context;

/**
 * Created by djp on 1/27/2017.
 *
 * Needed to allow access to the string resources for the application in the static data conversion classes in PetContract.java
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
