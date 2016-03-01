package com.destiner.social_reader.presenter.article_list;

import com.destiner.social_reader.model.structs.Article;

import java.util.List;

/**
 * Fires when articles are loaded
 */
public interface OnArticlesLoadListener {
    void onLoad(List<Article> articles);
}
