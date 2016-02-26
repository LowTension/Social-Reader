package com.destiner.social_reader.view.article_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.presenter.article_list.ArticleListPresenter;

import java.util.ArrayList;

public class ArticleListActivity extends AppCompatActivity implements ArticleListView {
    RecyclerView articleListRecyclerView;
    ArticleListAdapter adapter;

    ArticleListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        articleListRecyclerView = (RecyclerView) findViewById(R.id.article_list_recycler_view);
        articleListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleListAdapter(new ArrayList<Post>());
        articleListRecyclerView.setAdapter(adapter);

        presenter = new ArticleListPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
