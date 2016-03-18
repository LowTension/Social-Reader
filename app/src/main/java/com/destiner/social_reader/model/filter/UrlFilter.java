package com.destiner.social_reader.model.filter;

import com.destiner.social_reader.model.structs.Post;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Post passes this filter if it does not contains url links of any kind.
 */
public class UrlFilter extends Filter {
    private static final String URL_REGEX =
            "(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    @Override
    boolean isOk(Post post) {
        String text = post.getText();
        Matcher matcher = URL_PATTERN.matcher(text);
        // Posts that does not contain url pass.
        return !matcher.find();
    }
}
