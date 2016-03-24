package com.destiner.social_reader.view.article_list;

import android.support.design.widget.Snackbar;
import android.view.View;

public class ArchiveArticleSnackbar {
    private static int archived = 0;
    private static Snackbar last = null;

    public static void update(View view) {
        if (last == null || !last.isShown()) {
            // Last Snackbar that was shown died, reset archived counter
            archived = 0;
        }
        archived++;
        String articleSpelling = (archived == 1) ? "article" : "articles";
        String snackbarText = String.format("%d %s archived", archived, articleSpelling);
        last = Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG);
        last.show();
    }
}