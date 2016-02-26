package com.destiner.social_reader.model.structs.source;

import org.joda.time.DateTime;

/**
 * Structure that describes where the information come. Every post has its original source.
 */
public abstract class Source {
    private DateTime lastPostTime;
    private DateTime firstPostTime;

    public Source() {
        firstPostTime = new DateTime(Integer.MAX_VALUE);
        lastPostTime = new DateTime(Integer.MAX_VALUE);
    }

    public DateTime getFirstPostTime() {
        return firstPostTime;
    }
}
