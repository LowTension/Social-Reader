package com.destiner.social_reader;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Application entry point
 */
public class ReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
