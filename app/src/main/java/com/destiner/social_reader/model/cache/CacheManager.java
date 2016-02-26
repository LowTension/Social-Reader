package com.destiner.social_reader.model.cache;

import com.destiner.social_reader.model.explorer.Explorer;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.presenter.article_list.OnArticlesLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps posts. Gives posts when requested. Loads new posts if request exceeds amount of posts
 * cached.
 */
public class CacheManager {
    private static List<Post> postCache = new ArrayList<>();

    /**
     * Private constructor to forbid class instantiation
     */
    private CacheManager() {
    }

    /**
     * Retrieves posts from cache. If cache doesn't have specified elements, retrieves them from
     * Explorer class. After all, forms the specified posts in sublist and fires callback.
     * @param count number of element
     * @param offset offset in cache post list
     * @param callback callback listener will fire when posts will be ready
     */
    public static void getFromCache(int count, int offset, OnArticlesLoadListener callback) {
        if (postCache.size() < count + offset) {
            OnOffsetArticlesLoadListener listener = getListener(count, offset, callback);
            loadToCache(count + offset - postCache.size(), listener);
        } else {
            List<Post> requestedPosts = postCache.subList(offset, offset + count);
            callback.onLoad(requestedPosts);
        }
    }

    /**
     * Loads elements to store them in cache
     * @param length the minimal number of elements should be loaded
     */
    private static void loadToCache(int length, OnOffsetArticlesLoadListener listener) {
        Explorer.getNew(length, listener);
    }

    /**
     * Form listener with specified parameters
     * @param count count of posts required
     * @param offset offset in post list
     * @param callback callback listener will fire when posts will be ready
     * @return created listener
     */
    private static OnOffsetArticlesLoadListener getListener(int count, int offset,
                                                            final OnArticlesLoadListener callback) {
        return new OnOffsetArticlesLoadListener(count, offset) {
            @Override
            public void onLoad(List<Post> articles) {
                postCache.addAll(articles);
                int start = getOffset();
                int end = getOffset() + getCount();
                List<Post> requestedPosts = postCache.subList(start, end);
                callback.onLoad(requestedPosts);
            }
        };
    }
}
