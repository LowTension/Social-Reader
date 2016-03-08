package com.destiner.social_reader.view.article_list;

import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.view.View;

import java.util.List;

public interface ArticleListView extends View {
    void showArticle(Article article);
    void showArticles(List<Article> articles, boolean isNew);
}
