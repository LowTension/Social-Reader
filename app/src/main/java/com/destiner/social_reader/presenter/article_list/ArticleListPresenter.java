package com.destiner.social_reader.presenter.article_list;


import com.destiner.social_reader.model.cache.CacheManager;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.presenter.Presenter;
import com.destiner.social_reader.view.article_list.ArticleListView;

import java.util.List;

public class ArticleListPresenter implements Presenter<ArticleListView>, OnArticlesLoadListener {
    private ArticleListView view;

    /**
     * Loads articles from cache. When ready, callback is fired.
     * @param count count of articles required
     * @param offset offset in article list
     */
    public void loadArticles(int count, int offset) {
        CacheManager.getFromCache(count, offset, this);
    }

    /**
     * Removes article form cache.
     * @param article article to remove
     */
    public void deleteArticle(Article article) {
        CacheManager.deleteArticle(article);
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
    public void onLoad(List<Article> articles) {
        view.showArticles(articles);
    }
}
