package com.destiner.social_reader.model.cache;

import android.content.Context;

import com.destiner.social_reader.model.explorer.Explorer;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.listeners.articles_load.Content;
import com.destiner.social_reader.model.structs.listeners.articles_load.OnArticleRequestListener;
import com.destiner.social_reader.model.structs.listeners.articles_load.RequestError;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps articles. Gives articles when requested. Loads new articles if request exceeds amount of
 * articles cached.
 */
public class CacheManager {
    private static Context context;

    private static ArticleOpenHelper databaseHelper;

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
        int count = callback.getRequest().getCount();
        int offset = callback.getRequest().getOffset();
        if (databaseHelper.getCount() < count + offset) {
            OnArticleRequestListener listener = getListener(callback);
            loadToCache(count + offset - databaseHelper.getCount(), listener);
        } else {
            List<Article> requestedArticles = databaseHelper.get(count, offset);
        }
    }

    public static void setContext(Context c) {
        context = c;
        databaseHelper = new ArticleOpenHelper(context);
    }

    /**
     * Deletes article from cache.
     * @param article article to be deleted
     */
    public static void deleteArticle(Article article) {
        databaseHelper.delete(article);
    }

    /**
     * Loads elements to store them in cache
     * @param length the minimal number of elements should be loaded
     */
    private static void loadToCache(int length, OnArticleRequestListener listener) {
        Explorer.getNew(length, listener);
    }

    private static OnArticleRequestListener getListener(final OnArticleRequestListener callback) {
        return new OnArticleRequestListener(callback.getRequest()) {
            @Override
            public void onContentReady(Content content) {
                databaseHelper.addAll(content.getArticles());
                List<Article> requestedArticles = new ArrayList<>();
                callback.onContentReady(new Content(requestedArticles));
            }

            @Override
            public void onError(RequestError error) {
                callback.onError(error);
            }
        };
    }
}
