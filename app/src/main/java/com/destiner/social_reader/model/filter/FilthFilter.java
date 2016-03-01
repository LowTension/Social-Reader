package com.destiner.social_reader.model.filter;

import android.content.Context;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Post;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Post passes this filter if it does not contains abusive language.
 * All stop words declared in resource file words.xml
 */
public class FilthFilter extends Filter {
    private static Set<String> filthyWords;

    public FilthFilter(Context context) {
        addWordsFromResources(context);
    }

    @Override
    public boolean isOk(Post post) {
        String postText = post.getText();
        for (String word : filthyWords) {
            if (postText.contains(word)) {
                return false;
            }
        }
        return true;
    }

    private static void addWordsFromResources(Context context) {
        if (context == null) {
            return;
        }
        // Add sources
        String[] words = context.getResources().getStringArray(R.array.filthy_words_ru);
        filthyWords = new HashSet<>();
        Collections.addAll(filthyWords, words);
    }
}
