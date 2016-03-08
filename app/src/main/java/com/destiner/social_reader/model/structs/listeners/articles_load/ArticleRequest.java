package com.destiner.social_reader.model.structs.listeners.articles_load;

public class ArticleRequest {
    private int count;
    private int offset;

    public ArticleRequest(int count, int offset) {
        this.count = count;
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public int getOffset() {
        return offset;
    }
}
