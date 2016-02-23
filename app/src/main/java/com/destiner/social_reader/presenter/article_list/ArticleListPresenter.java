package com.destiner.social_reader.presenter.article_list;


import com.destiner.social_reader.presenter.Presenter;
import com.destiner.social_reader.view.article_list.ArticleListView;

public class ArticleListPresenter implements Presenter<ArticleListView> {

    private ArticleListView view;

    @Override
    public void attachView(ArticleListView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
