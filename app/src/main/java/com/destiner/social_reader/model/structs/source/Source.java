package com.destiner.social_reader.model.structs.source;

/**
 * Structure that describes where the information come. Every post has its original source.
 */
public abstract class Source {
    public Source() {
    }

    public abstract String getStringId();
}
