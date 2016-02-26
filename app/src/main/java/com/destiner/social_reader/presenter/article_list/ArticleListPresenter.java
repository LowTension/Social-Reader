package com.destiner.social_reader.presenter.article_list;


import com.destiner.social_reader.model.cache.CacheManager;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.presenter.Presenter;
import com.destiner.social_reader.view.article_list.ArticleListView;

import java.util.List;

public class ArticleListPresenter implements Presenter<ArticleListView>, OnArticlesLoadListener {
    private ArticleListView view;

    /**
     * Loads articles from cache. When ready, callback is fired.
     * @param count count of posts required
     * @param offset offset in post list
     */
    public void loadArticles(int count, int offset) {
        CacheManager.getFromCache(count, offset, this);
    }

    @Override
    public void attachView(ArticleListView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onLoad(List<Post> posts) {
    }
}
