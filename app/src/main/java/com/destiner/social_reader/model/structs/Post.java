package com.destiner.social_reader.model.structs;

/**
 * Post structure. Contains information got from API request.
 * Content will be displayed to user.
 */
public class Post {
    String text;

    public Post(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
