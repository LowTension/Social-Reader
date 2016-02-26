package com.destiner.social_reader.presenter.article_list;

import com.destiner.social_reader.model.structs.Post;

import java.util.List;

/**
 * Fires when posts are loaded
 */
public interface OnArticlesLoadListener {
    void onLoad(List<Post> articles);
}
