package com.destiner.social_reader.view.article_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.destiner.social_reader.R;
import com.destiner.social_reader.model.structs.Article;

import java.util.List;

/**
 * Adapter for article list
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private ArticleListView view;

    private List<Article> dataSet;

    /**
     * Custom ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView articleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            articleTextView = (TextView) itemView.findViewById(R.id.article_text);
        }

        @Override
        public void onClick(View v) {
            view.showArticle(dataSet.get(getAdapterPosition()));
        }
    }

    public ArticleListAdapter(ArticleListView view, List<Article> articles) {
        this.view = view;
        dataSet = articles;
    }

    /**
     * Adds article to the data set and notifies RecyclerView
     * @param article article to be added
     */
    public void add(Article article) {
        dataSet.add(article);
        notifyItemInserted(dataSet.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_element_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = dataSet.get(position);
        holder.articleTextView.setText(article.getText());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
