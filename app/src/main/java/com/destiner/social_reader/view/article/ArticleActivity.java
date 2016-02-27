package com.destiner.social_reader.view.article;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        if (post == null) {
            return;
        }
        articleTextView.setText(post.getText());
    }
}
