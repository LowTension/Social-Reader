package com.destiner.social_reader.view.article_list;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.destiner.social_reader.R;

public class ArticleListActivity extends AppCompatActivity implements ArticleListView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
