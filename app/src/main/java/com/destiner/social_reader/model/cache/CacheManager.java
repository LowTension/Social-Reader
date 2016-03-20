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
    private static Context context;

    private static ArticleOpenHelper databaseHelper;

    private static SharedPreferences preferences;
    // Name of SharedPreferences that store information to support the CacheManager
    private static final String PREFERENCES_NAME = "cache_info";
    private static final String PREFERENCES_KEY_LAST = "last";
    private static int last = -1;

    private static final int AVAILABLE_POSTS = 20;

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
        int size = databaseHelper.getCount();
        if (offset < 0) {
            if (last + count < size) {
                // New articles have been already loaded
                getOldArticles(callback);
                // If there are too little new posts available in cache
                if (size - last < AVAILABLE_POSTS) {
                    int newPostCount = 20;
                    loadToCache(newPostCount, getSilentListener());
                }
            } else {
                // Load new articles
                getNewArticles(callback, count, offset);
            }
        } else if (offset == 0) {
            if (count < size) {
                // Database has enough articles; return them
                getOldArticles(callback);
            } else {
                // Load new articles to the database
                getNewArticles(callback, count, offset);
            }
        } else {
            // Positive offset means try to get existing items anyway
            getOldArticles(callback);
        }
    }

    public static void setContext(Context c) {
        context = c;
        databaseHelper = new ArticleOpenHelper(context);
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        last = preferences.getInt(PREFERENCES_KEY_LAST, -1);
    }

    /**
     * Deletes article from cache.
     * @param article article to be deleted
     */
    public static void deleteArticle(Article article) {
        last--;
        preferences.edit().putInt(PREFERENCES_KEY_LAST, last).apply();
        databaseHelper.delete(article);
    }

    private static void getNewArticles(OnArticleRequestListener callback, int count, int offset) {
        OnArticleRequestListener listener = getListener(callback);
        loadToCache(count + offset - databaseHelper.getCount(), listener);
    }

    private static void getOldArticles(OnArticleRequestListener callback) {
        List<Article> requestedArticles = getArticles(callback.getRequest());
        callback.onContentReady(new Content(requestedArticles));
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
                List<Article> requestedArticles = getArticles(callback.getRequest());
                callback.onContentReady(new Content(requestedArticles));
            }

            @Override
            public void onError(RequestError error) {
                callback.onError(error);
            }
        };
    }

    private static OnArticleRequestListener getSilentListener() {
        return new OnArticleRequestListener(null) {
            @Override
            public void onContentReady(Content content) {
                List<Article> articles = content.getArticles();
                databaseHelper.addAll(articles);
            }

            @Override
            public void onError(RequestError error) {
            }
        };
    }

    private static Bounds calculateBounds(ArticleRequest request) {
        int count = request.getCount();
        int offset = request.getOffset();
        int upper;
        int lower;
        int size = databaseHelper.getCount();
        if (last == -1) {
            // Get top most; ignore offset
            upper = size - 1;
            lower = upper - count + 1;
        } else {
            // Start from last
            lower = last + 1 - offset - count;
            upper = lower + count - 1;
        }
        return new Bounds(upper, lower);
    }

    private static List<Article> getArticles(ArticleRequest request) {
        Bounds bounds = calculateBounds(request);
        int count;
        int offset;
        if (!bounds.isValid()) {
            // If there is something available (but not everything that was requested)
            if (bounds.getUpper() >= 0) {
                count = bounds.getUpper() + 1;
                offset = 0;
            } else {
                return new ArrayList<>();
            }
        } else {
            // Cache has everything that was requested
            count = bounds.getUpper() - bounds.getLower() + 1;
            offset = bounds.getLower();
        }
        List<Article> articles = databaseHelper.get(count, offset);
        Collections.reverse(articles);
        last = Math.max(last, bounds.getUpper());
        preferences.edit().putInt(PREFERENCES_KEY_LAST, last).apply();
        return articles;
    }

    /**
     * AsyncTask that loads new posts in background. As the content fully downloaded from server,
     * stores it in SQLite database to retrieve later.
     */
    private static class PostLoadTask extends AsyncTask {
        private static final int AVAILABLE_POSTS = 20;

        @Override
        protected Object doInBackground(Object[] params) {
            CacheManager.loadToCache(20, getCallback());
            return null;
        }

        private OnArticleRequestListener getCallback() {
            ArticleRequest request = new ArticleRequest(20, -20);
            return new OnArticleRequestListener(request) {
                @Override
                public void onContentReady(Content content) {
                    List<Article> articles = content.getArticles();
                    databaseHelper.addAll(articles);
                    // We should delay our requests to not exceed limit of API method calls
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // If what's loaded is not enough
                    if (databaseHelper.getCount() - last < AVAILABLE_POSTS) {
                        // Load more
                        CacheManager.loadToCache(AVAILABLE_POSTS, getCallback());
                    }
                }

                @Override
                public void onError(RequestError error) {
                }
            };
        }
    }
}
