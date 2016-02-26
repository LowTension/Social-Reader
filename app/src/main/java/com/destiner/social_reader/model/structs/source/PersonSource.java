package com.destiner.social_reader.model.structs.source;

/**
 * Structure describes source that is a VK personal page.
 */
public class PersonSource extends Source {

    private int id;

    public PersonSource(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
