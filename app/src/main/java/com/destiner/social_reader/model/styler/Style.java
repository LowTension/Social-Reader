package com.destiner.social_reader.model.styler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines some style. Style is a rule of what should be replaced with what. All styles should
 * implement this class by providing original regular expression which should be replaced and
 * what will be instead. As an example, to create style that removes all 'a' letters, getOriginal()
 * should return String "a" and getReplacement() should return an empty String.
 */
public abstract class Style {
    public String styleText(String originalText) {
        String originalExpression = getOriginal();
        String replaceExpression = getReplacement();
        Pattern pattern = Pattern.compile(originalExpression);
        Matcher matcher = pattern.matcher(originalText);
        return matcher.replaceAll(replaceExpression);
    }

    protected abstract String getOriginal();

    protected abstract String getReplacement();
}
