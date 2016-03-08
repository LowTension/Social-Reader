package com.destiner.social_reader.model.cache;

import android.content.Context;

import com.destiner.social_reader.model.explorer.Explorer;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.listeners.articles_load.OnArticleRequestListener;

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
     * @param count number of element
     * @param offset offset in cache article list
     */
    public static void getFromCache(int count, int offset) {
        if (databaseHelper.getCount() < count + offset) {
            OnArticleRequestListener listener = getListener(count, offset);
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

    /**
     * Form listener with specified parameters
     * @param count count of articles required
     * @param offset offset in article list
     * @return created listener
     */
    private static OnArticleRequestListener getListener(int count, int offset) {
        return new OnArticleRequestListener(count, offset) {
            @Override
            public void onLoad(List<Article> articles) {
                databaseHelper.addAll(articles);
                List<Article> requestedArticles = databaseHelper.get(getCount(), getOffset());
            }
        };
    }
}
