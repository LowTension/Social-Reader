package com.destiner.social_reader.model.cache;


import android.content.Context;

import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.listeners.articles_load.ArticleRequest;

import java.util.List;

public class ArchiveCache {
    static ArchiveOpenHelper databaseHelper;

    /**
     * Private constructor to forbid class instantiation
     */
    private ArchiveCache() {
    }

    static void initialize(Context context) {
        databaseHelper = new ArchiveOpenHelper(context);
    }

    static void add(Article article) {
        databaseHelper.add(article);
    }

    static List<Article> get(ArticleRequest request) {
        return databaseHelper.get(request.getCount(), request.getOffset());
    }

    static void delete(Article article) {
        databaseHelper.delete(article);
    }
}
