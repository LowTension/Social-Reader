package com.destiner.social_reader.model.structs.source;

import org.joda.time.DateTime;

/**
 * Structure that describes where the information come. Every post has its original source.
 */
public abstract class Source {
    private final long maxUnixTime = Integer.MAX_VALUE;
    private final long maxUnixTimeMillis = maxUnixTime * 1000;

    private DateTime lastPostTime;
    private DateTime firstPostTime;

    public Source() {
        firstPostTime = new DateTime(maxUnixTimeMillis);
        lastPostTime = new DateTime(maxUnixTimeMillis);
    }

    public DateTime getFirstPostTime() {
        return firstPostTime;
    }

    public void setFirstPostTime(DateTime firstPostTime) {
        this.firstPostTime = firstPostTime;
    }
}
