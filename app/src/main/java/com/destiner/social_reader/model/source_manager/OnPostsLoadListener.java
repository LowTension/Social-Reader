package com.destiner.social_reader.model.source_manager;

import com.destiner.social_reader.model.cache.OnOffsetArticlesLoadListener;
import com.destiner.social_reader.model.structs.Article;
import com.destiner.social_reader.model.structs.Post;
import com.destiner.social_reader.model.structs.source.Source;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;

/**
 * Wrapper for OnOffsetArticlesLoadListener that keeps queried sources.
 */
public abstract class OnPostsLoadListener extends OnOffsetArticlesLoadListener {
    private Set<? extends Source> sources;
    private OnOffsetArticlesLoadListener callback;

    public OnPostsLoadListener(Set<? extends Source> sources, OnOffsetArticlesLoadListener listener)
    {
        super(listener.getCount(), listener.getOffset());
        this.sources = sources;
        this.callback = listener;
    }

    public abstract void onPostsLoad(List<Post> posts, DateTime earliestPostDate);

    public void onLoad(List<Article> articles) {
        callback.onLoad(articles);
    }

    public Set<? extends Source> getSources() {
        return sources;
    }
}