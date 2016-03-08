package com.destiner.social_reader.model.structs.listeners.articles_load;

import com.destiner.social_reader.model.structs.Article;

import java.util.List;

public class Content {
    private List<Article> articles;

    public Content(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
