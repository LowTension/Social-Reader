package com.destiner.social_reader.model.source_manager;

import android.util.Log;

import com.destiner.social_reader.model.filter.FilterManager;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.model.cache.OnOffsetArticlesLoadListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended version of VKRequestListener that keeps reference to the callback.
 * This callback fires when request is complete.
 */
public class SourceVKRequestListener extends VKRequest.VKRequestListener {
    OnOffsetArticlesLoadListener listener;

    private static String TAG = "SourceVKRequestListener";

    public SourceVKRequestListener(OnOffsetArticlesLoadListener callback) {
        listener = callback;
    }

    @Override
    public void onComplete(VKResponse response) {
        // Get posts array as JSON
        List<Post> posts = new ArrayList<>();
        JSONArray postArray = null;
        try {
            postArray = response.json.getJSONObject("response").getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (postArray == null) {
            return;
        }
        // Get post text from each post
        for (int i = 0; i < postArray.length(); i++) {
            String postText = null;
            try {
                postText = postArray.getJSONObject(i).getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (postText == null) {
                continue;
            }
            // Create post instance with text and add it to the list
            Post post = new Post(postText);
            posts.add(post);
        }
        FilterManager.filter(posts, listener);
    }

    @Override
    public void onError(VKError error) {
        Log.i(TAG, error.toString());
    }
}
