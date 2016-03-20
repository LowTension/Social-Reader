package com.destiner.social_reader.view.article_list;

import android.support.design.widget.Snackbar;
import android.view.View;

public class DeleteArticleSnackbar {
    private static int deleted = 0;
    private static Snackbar last = null;

    public static void update(View view) {
        if (last == null || !last.isShown()) {
            // Last Snackbar that was shown died, reset deleted counter
            deleted = 0;
        }
        deleted++;
        String articleSpelling = (deleted == 1) ? "article" : "articles";
        String snackbarText = String.format("%d %s deleted", deleted, articleSpelling);
        last = Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG);
        last.show();
    }
}