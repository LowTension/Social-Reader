package com.destiner.social_reader.model.cache;

import com.destiner.social_reader.model.explorer.Explorer;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.presenter.article_list.OnArticlesLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps articles. Gives articles when requested. Loads new articles if request exceeds amount of
 * articles cached.
 */
public class CacheManager {
    private static List<Article> articleCache = new ArrayList<>();

    /**
     * Private constructor to forbid class instantiation
     */
    private CacheManager() {
    }

    /**
     * Retrieves articles from cache. If cache doesn't have specified elements, retrieves them from
     * Explorer class. After all, forms the specified articles in sublist and fires callback.
     * @param count number of element
     * @param offset offset in cache article list
     * @param callback callback listener will fire when articles will be ready
     */
    public static void getFromCache(int count, int offset, OnArticlesLoadListener callback) {
        if (articleCache.size() < count + offset) {
            OnOffsetArticlesLoadListener listener = getListener(count, offset, callback);
            loadToCache(count + offset - articleCache.size(), listener);
        } else {
            List<Article> requestedArticles = articleCache.subList(offset, offset + count);
            callback.onLoad(requestedArticles);
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
     * @param count count of articles required
     * @param offset offset in article list
     * @param callback callback listener will fire when articles will be ready
     * @return created listener
     */
    private static OnOffsetArticlesLoadListener getListener(int count, int offset,
                                                            final OnArticlesLoadListener callback) {
        return new OnOffsetArticlesLoadListener(count, offset) {
            @Override
            public void onLoad(List<Article> articles) {
                articleCache.addAll(articles);
                int start = getOffset();
                int end = getOffset() + getCount();
                List<Article> requestedArticles = articleCache.subList(start, end);
                callback.onLoad(requestedArticles);
            }
        };
    }
}
