package olegkuro.learnbyear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import olegkuro.learnbyear.loader.SearchResult;

/**
 * Created by Roman on 12/12/2016.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultHolder> {
    private List<SearchResult> searchResults = Collections.emptyList();
    private Context context;
    private LayoutInflater layoutInflater;
    // RecyclerView has no OnItemClickListener (unlike ListView) so custom listener is used
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SearchResultAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SearchResultHolder.newInstance(parent, layoutInflater);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int position) {
        SearchResult searchResult = searchResults.get(position);
        holder.title.setText(searchResult.title);
        holder.author.setText(searchResult.author);
        holder.bind(position, listener);
    }

    static class SearchResultHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;

        public SearchResultHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
        }

        public static SearchResultHolder newInstance(ViewGroup parent, LayoutInflater inflater) {
            View itemView = inflater.inflate(R.layout.search_result_item, parent);
            return new SearchResultHolder(itemView);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}
