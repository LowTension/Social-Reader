package com.destiner.social_reader.view.article;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Post;

public class ArticleActivity extends AppCompatActivity {
    Post post;

    TextView articleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        post = (Post) getIntent().getExtras().getSerializable("article");

        bindUI();
        setUI();

    }

    private void bindUI() {
        articleTextView = (TextView) findViewById(R.id.article_text);
    }

    private void setUI() {
        setToolbar();
        if (post == null) {
            return;
        }
        articleTextView.setText(post.getText());
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
