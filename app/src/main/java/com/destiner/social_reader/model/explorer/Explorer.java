package com.destiner.social_reader.model.explorer;

import com.destiner.social_reader.model.cache.OnArticleRequestListener;
import com.destiner.social_reader.model.source_manager.SourceManager;

/**
 * Loads new posts from different sources.
 */
public class Explorer {

    /**
     * Private constructor to forbid class instantiation
     */
    private Explorer() {
    }

    /**
     * Loads posts
     * @param length minimal number of posts should be loaded
     * @param callback callback listener will fire when request will complete
     */
    public static void getNew(int length, OnArticleRequestListener callback) {
        // TODO use length parameter to load at least 'length' posts
        SourceManager.getGroupPosts(callback);
    }
}
