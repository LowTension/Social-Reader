package com.destiner.social_reader.model.styler;

import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.listeners.articles_load.Content;
import com.destiner.social_reader.model.structs.listeners.articles_load.OnArticleRequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses Styles to style articles. Styles each article one by one by applying all registered styles
 * to each article one by one.
 */
public class StyleManager {
    private static List<Style> styles = new ArrayList<>();

    static {
        registerStyles();
    }

    public static void style(List<Article> articles, OnArticleRequestListener callback) {
        // Processes all articles
        for (Article article : articles) {
            String articleText = article.getText();
            // Applies each style
            for (Style style : styles) {
                articleText = style.styleText(articleText);
            }
            article.setText(articleText);
        }

        // Fires callback
        Content content = new Content(articles);
        callback.onContentReady(content);
    }

    private static void registerStyles() {
        styles.add(new NoHashTagStyle());
        styles.add(new NoAtTagStyle());
    }
}
