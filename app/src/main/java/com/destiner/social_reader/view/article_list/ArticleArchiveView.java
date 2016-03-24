package com.destiner.social_reader.view.article_list;

import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.view.View;

import java.util.List;

public interface ArticleArchiveView extends View, ArticleViewer {
    void showArticles(List<Article> articles);
}
