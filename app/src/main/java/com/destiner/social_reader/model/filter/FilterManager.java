package com.destiner.social_reader.model.filter;

import android.content.Context;

import com.destiner.social_reader.model.structs.listeners.articles_load.Content;
import com.destiner.social_reader.model.structs.listeners.articles_load.OnArticleRequestListener;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.model.styler.StyleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses filters to get articles from posts. Applies each filter one by one. Every post that passes
 * becomes an article.
 */
public class FilterManager {
    private static Context context;

    private static List<Filter> filters = new ArrayList<>();

    /**
     * Applies filters. Fire callback when articles are ready.
     * @param posts list of posts
     * @param callback callback listener
     */
    public static void filter(List<Post> posts, OnArticleRequestListener callback) {
        // Applies filters
        for (Filter filter : filters) {
            posts = filter.filter(posts);
        }
        // Creates article instances based on posts text
        List<Article> articles = new ArrayList<>();
        for (Post filteredPost : posts) {
            articles.add(new Article(filteredPost.getText()));
        }
        StyleManager.style(articles, callback);
    }

    public static void setContext(Context c) {
        context = c;
        registerFilters();
    }

    private static void registerFilters() {
        filters.add(new LengthFilter());
        filters.add(new FilthFilter(context));
        filters.add(new InnerLinksFilter());
    }
}
