package com.destiner.social_reader.view.article_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.presenter.article_list.ArticleListPresenter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;

public class ArticleListActivity extends AppCompatActivity implements ArticleListView {
    RecyclerView articleListRecyclerView;
    ArticleListAdapter adapter;

    ArticleListPresenter presenter;

    final String[] scope = {VKScope.FRIENDS, VKScope.WALL};

    private final String TAG = "ArticleListActivity";

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

        VKSdk.login(this, scope);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, loginCallback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private VKCallback<VKAccessToken> loginCallback = new VKCallback<VKAccessToken>() {
        @Override
        public void onResult(VKAccessToken res) {
            presenter.loadArticles(10, 0);
        }

        @Override
        public void onError(VKError error) {
            Log.i(TAG, error.toString());
        }
    };
}
