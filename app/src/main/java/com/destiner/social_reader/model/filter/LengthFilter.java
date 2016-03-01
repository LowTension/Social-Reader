package com.destiner.social_reader.model.filter;

import com.destiner.social_reader.model.structs.Post;

/**
 * Post passes this filter if its length lies in specified bounds.
 */
public class LengthFilter extends Filter {
    private static final int LOWER_BOUND = 1200;

    @Override
    public boolean isOk(Post post) {
        return post.getText().length() >= 1200;
    }
}
