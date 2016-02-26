package com.destiner.social_reader.view.article_list;

import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.view.View;

import java.util.List;

public interface ArticleListView extends View {
    void showArticles(List<Post> articles);
}
