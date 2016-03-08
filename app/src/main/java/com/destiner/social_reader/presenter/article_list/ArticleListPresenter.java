package com.destiner.social_reader.presenter.article_list;


import com.destiner.social_reader.model.cache.CacheManager;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.listeners.articles_load.ArticleRequest;
import com.destiner.social_reader.model.structs.listeners.articles_load.Content;
import com.destiner.social_reader.model.structs.listeners.articles_load.OnArticleRequestListener;
import com.destiner.social_reader.model.structs.listeners.articles_load.RequestError;
import com.destiner.social_reader.presenter.Presenter;
import com.destiner.social_reader.view.article_list.ArticleListView;

import java.util.List;

public class ArticleListPresenter implements Presenter<ArticleListView> {
    private ArticleListView view;

    /**
     * Loads articles from cache. When ready, callback is fired.
     * @param count count of articles required
     * @param offset offset in article list
     */
    public void loadArticles(int count, int offset) {
        ArticleRequest request = new ArticleRequest(count, offset);
        CacheManager.getFromCache(getListener(request));
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

    private OnArticleRequestListener getListener(ArticleRequest request) {
        return new OnArticleRequestListener(request) {
            @Override
            public void onContentReady(Content content) {
                boolean isNew = getRequest().getOffset() < 0;
                view.showArticles(content.getArticles(), isNew);
            }

            @Override
            public void onError(RequestError error) {
                switch (error.getCode()) {
                    case NO_CONNECTION:
                        // TODO notify View
                        break;
                    case NO_REPLY:
                        // TODO notify View
                        break;
                }
            }
        };
    }
}
