package com.destiner.social_reader.model.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.destiner.social_reader.model.explorer.Explorer;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.Bounds;
import com.destiner.social_reader.model.structs.listeners.articles_load.ArticleRequest;
import com.destiner.social_reader.model.structs.listeners.articles_load.Content;
import com.destiner.social_reader.model.structs.listeners.articles_load.OnArticleRequestListener;
import com.destiner.social_reader.model.structs.listeners.articles_load.RequestError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Keeps articles. Gives articles when requested. Loads new articles if request exceeds amount of
 * articles cached.
 */
public class CacheManager {
    /**
     * Private constructor to forbid class instantiation
     */
    private CacheManager() {
    }

    /**
     * Retrieves articles from cache. If cache doesn't have specified elements, retrieves them from
     * Explorer class. After all, forms the specified articles in sublist and fires callback.
     * @param callback callback listener will fire when articles will be ready
     */
    public static void getFromCache(OnArticleRequestListener callback) {
        ArticleCache.get(callback);
    }

    public static void setContext(Context c) {
        ArticleCache.initialize(c);
    }

    /**
     * Deletes article from cache.
     * @param article article to be deleted
     */
    public static void deleteArticle(Article article) {
        ArticleCache.delete(article);
    }
}
