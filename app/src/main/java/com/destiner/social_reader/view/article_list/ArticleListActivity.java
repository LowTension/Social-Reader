package com.destiner.social_reader.view.article_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.presenter.article_list.ArticleListPresenter;
import com.destiner.social_reader.view.article.ArticleActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements ArticleListView {
    RecyclerView articleListRecyclerView;
    ArticleListAdapter adapter;
    LinearLayoutManager layoutManager;

    ArticleListPresenter presenter;

    private static final int oldArticles = 10;
    private boolean loadingOld = false;

    final String[] scope = {VKScope.FRIENDS, VKScope.WALL};

    private static final String TAG = "ArticleListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        setUI();

        presenter = new ArticleListPresenter();
        presenter.attachView(this);

        if (VKAccessToken.currentToken() == null || VKAccessToken.currentToken().isExpired()) {
            VKSdk.login(this, scope);
        } else {
            loadContent();
        }
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
    public void showArticle(Article article) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public void showArticles(List<Article> articles, boolean isNew) {
        loadingOld = isNew && loadingOld;
        for (Article article : articles) {
            adapter.add(article);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, loginCallback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setUI() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        articleListRecyclerView = (RecyclerView) findViewById(R.id.article_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        articleListRecyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleListAdapter(this, new ArrayList<Article>());
        articleListRecyclerView.setAdapter(adapter);
        articleListRecyclerView.addOnItemTouchListener(getSwipeableTouchListener());
        articleListRecyclerView.addOnScrollListener(getOnScrollDownListener());
    }

    private VKCallback<VKAccessToken> loginCallback = new VKCallback<VKAccessToken>() {
        @Override
        public void onResult(VKAccessToken res) {
            loadContent();
        }

        @Override
        public void onError(VKError error) {
            Log.i(TAG, error.toString());
        }
    };

    private void loadContent() {
        presenter.loadArticles(20, 0);
    }

    private SwipeableRecyclerViewTouchListener getSwipeableTouchListener() {
        return new SwipeableRecyclerViewTouchListener(articleListRecyclerView, getSwipeListener());
    }

    private SwipeableRecyclerViewTouchListener.SwipeListener getSwipeListener() {
        return new SwipeableRecyclerViewTouchListener.SwipeListener() {
            @Override
            public boolean canSwipeLeft(int position) {
                return true;
            }

            @Override
            public boolean canSwipeRight(int position) {
                return false;
            }

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] positions) {
                for (int position : positions) {
                    Article article = adapter.get(position);
                    // Delete article from storage
                    presenter.deleteArticle(article);
                    // Delete article from RecyclerView
                    adapter.remove(position);
                }
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] positions) {
            }
        };
    }

    private RecyclerView.OnScrollListener getOnScrollDownListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int index = layoutManager.findLastVisibleItemPosition();
                if (index > adapter.getItemCount() - 3 && !loadingOld) {
                    loadingOld = true;
                    presenter.loadArticles(oldArticles, adapter.getItemCount());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };
    }
}
