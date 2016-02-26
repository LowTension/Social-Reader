package com.destiner.social_reader.model.structs.source;

/**
 * Structure describes source that is a VK group.
 */
public class GroupSource extends Source {
    private int id;

    public GroupSource(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
