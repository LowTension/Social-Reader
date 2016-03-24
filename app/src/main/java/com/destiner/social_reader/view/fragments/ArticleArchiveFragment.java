package com.destiner.social_reader.view.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.presenter.article_list.ArticleArchivePresenter;
import com.destiner.social_reader.view.article.ArticleActivity;
import com.destiner.social_reader.view.article_list.ArticleArchiveView;
import com.destiner.social_reader.view.article_list.ArticleListAdapter;
import com.destiner.social_reader.view.article_list.DeleteArticleSnackbar;
import com.destiner.social_reader.view.article_list.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleArchiveFragment extends Fragment implements ArticleArchiveView {
    private ArticleArchivePresenter presenter;

    private View fragmentView;

    private RecyclerView articleArchiveRecyclerView;
    private ArticleListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final int oldArticles = 10;
    private boolean loadingOld = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ArticleArchivePresenter();
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_article_archive, container, false);
        setUI();
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
        loadContent();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void showArticles(List<Article> articles) {
        for (Article article : articles) {
            adapter.add(article);
        }
        loadingOld = false;
    }

    @Override
    public void showArticle(Article article) {
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    private void setUI() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        articleArchiveRecyclerView = (RecyclerView)
                fragmentView.findViewById(R.id.article_archive_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        articleArchiveRecyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleListAdapter(this, new ArrayList<Article>());
        articleArchiveRecyclerView.setAdapter(adapter);
        articleArchiveRecyclerView.addOnItemTouchListener(getSwipeableTouchListener());
        articleArchiveRecyclerView.addOnScrollListener(getOnScrollDownListener());
    }

    private SwipeableRecyclerViewTouchListener getSwipeableTouchListener() {
        return new SwipeableRecyclerViewTouchListener(articleArchiveRecyclerView, getSwipeListener());
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

    private void loadContent() {
        presenter.loadArticles(20, 0);
    }
}
