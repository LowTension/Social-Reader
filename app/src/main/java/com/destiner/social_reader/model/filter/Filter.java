package com.destiner.social_reader.model.filter;

import com.destiner.social_reader.model.structs.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes rule. Post that do not match this rule is not an article. Can form list of posts that
 * passes check defined in filter.
 */
public abstract class Filter {
    /**
     * Tells whether post passes rule.
     * The rule itself should be described there as boolean condition.
     * @param post given post
     * @return result of check
     */
    abstract boolean isOk(Post post);

    /**
     * Returns list of posts that passes this particular filter.
     * @param posts list of posts
     * @return list of passes posts
     */
    List<Post> filter(List<Post> posts) {
        List<Post> failedPosts = new ArrayList<>();
        for (Post post : posts) {
            if (!isOk(post)) {
                failedPosts.add(post);
            }
        }
        posts.removeAll(failedPosts);
        return posts;
    }
}
