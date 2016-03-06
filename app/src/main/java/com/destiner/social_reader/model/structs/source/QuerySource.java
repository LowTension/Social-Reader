package com.destiner.social_reader.model.structs.source;

/**
 * Structure describes source that is a search query. Requests for this source will be formed as
 * a search from all posts in VK that contain query keyword.
 */
public class QuerySource extends Source {
    String query;

    public QuerySource(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public String getStringId() {
        return query;
    }
}
