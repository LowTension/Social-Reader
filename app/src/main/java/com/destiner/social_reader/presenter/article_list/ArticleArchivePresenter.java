package com.destiner.social_reader.presenter.article_list;

import com.destiner.social_reader.model.cache.CacheManager;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.listeners.articles_load.ArticleRequest;
import com.destiner.social_reader.presenter.Presenter;
import com.destiner.social_reader.view.article_list.ArticleArchiveView;

import java.util.List;

public class ArticleArchivePresenter implements Presenter<ArticleArchiveView> {
    private ArticleArchiveView view;

    @Override
    public void attachView(ArticleArchiveView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadArticles(int count, int offset) {
        ArticleRequest request = new ArticleRequest(count, offset);
        List<Article> articles = CacheManager.getArchivedArticles(request);
        view.showArticles(articles);
    }

    public void deleteArticle(Article article) {
        CacheManager.deleteArchivedArticle(article);
    }
}
