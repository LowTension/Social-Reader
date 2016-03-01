package com.destiner.social_reader;

import android.app.Application;

import com.destiner.social_reader.model.filter.FilterManager;
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
        // Set Context to load sources in SourceManager
        SourceManager.setContext(this);
        // Set Context to load filter parameters in FilterManager
        FilterManager.setContext(this);
    }
}
