package com.destiner.social_reader;

import android.app.Application;

import com.destiner.social_reader.model.source_manager.SourceManager;
import com.vk.sdk.VKSdk;

/**
 * Application entry point
 */
public class ReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        // Set ContextWrapper to load sources in SourceManager
        SourceManager.setContextWrapper(this);
    }
}
