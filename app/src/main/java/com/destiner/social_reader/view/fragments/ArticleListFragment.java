package com.destiner.social_reader.view.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.presenter.article_list.ArticleListPresenter;
import com.destiner.social_reader.view.article.ArticleActivity;
import com.destiner.social_reader.view.article_list.ArticleListAdapter;
import com.destiner.social_reader.view.article_list.ArticleListView;
import com.destiner.social_reader.view.article_list.DeleteArticleSnackbar;
import com.destiner.social_reader.view.article_list.SwipeableRecyclerViewTouchListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.List;

public class ArticleListFragment extends Fragment implements ArticleListView {
    ArticleListPresenter presenter;

    View fragmentView;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView articleListRecyclerView;
    ArticleListAdapter adapter;
    LinearLayoutManager layoutManager;

    private static final String TAG = "Article List";
    final String[] scope = {VKScope.FRIENDS, VKScope.WALL};

    private static final int oldArticles = 10;
    private static final int newArticles = 5;
    private boolean loadingOld = false;
    private boolean loadingNew = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ArticleListPresenter();
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_article_list, container, false);
        setUI();
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);

        if (VKAccessToken.currentToken() == null || VKAccessToken.currentToken().isExpired()) {
            VKSdk.login(this, scope);
        } else {
            loadContent();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, loginCallback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showArticle(Article article) {
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public void showArticles(List<Article> articles, boolean isNew) {
        if (isNew) {
            // Showing new articles
            adapter.addToStart(articles);
            loadingNew = false;
            // Update UI
            swipeRefreshLayout.setRefreshing(false);
        } else {
            // Showing old articles
            for (Article article : articles) {
                adapter.add(article);
            }
            loadingOld = false;
        }
    }

    @Override
    public void notifyNoConnection() {
        loadingNew = false;
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(articleListRecyclerView, "No connection", Snackbar.LENGTH_SHORT)
                .show();
    }

    private void setUI() {
        setRefreshLayout();
        setRecyclerView();
    }

    private void setRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout)
                fragmentView.findViewById(R.id.article_list_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(getRefreshListener());
    }

    private void setRecyclerView() {
        articleListRecyclerView = (RecyclerView)
                fragmentView.findViewById(R.id.article_list_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        articleListRecyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleListAdapter(this, new ArrayList<Article>());
        articleListRecyclerView.setAdapter(adapter);
        articleListRecyclerView.addOnItemTouchListener(getSwipeableTouchListener());
        articleListRecyclerView.addOnScrollListener(getOnScrollDownListener());
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
                    DeleteArticleSnackbar.update(recyclerView);
                }
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] positions) {
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!loadingNew) {
                    loadingNew = true;
                    presenter.loadArticles(newArticles, -newArticles);
                }
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
}
