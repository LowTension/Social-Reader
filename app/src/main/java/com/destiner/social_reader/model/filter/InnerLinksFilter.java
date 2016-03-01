package com.destiner.social_reader.model.filter;

import com.destiner.social_reader.model.structs.Post;

import java.util.regex.Pattern;

/**
 * Post passes this filter if it does not contain links to communities and persons.
 */
public class InnerLinksFilter extends Filter {
    @Override
    boolean isOk(Post post) {
        Pattern groupPattern = Pattern.compile("\\[club\\d*?\\|.*?\\]");
        Pattern personPattern = Pattern.compile("\\[id\\d*?\\|.*?\\]");
        boolean hasGroupLink = groupPattern.matcher(post.getText()).find();
        boolean hasPersonLink = personPattern.matcher(post.getText()).find();
        return !hasGroupLink && !hasPersonLink;
    }
}
