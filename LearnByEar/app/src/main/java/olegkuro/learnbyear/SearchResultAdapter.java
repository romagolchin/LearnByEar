package olegkuro.learnbyear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import olegkuro.learnbyear.loaders.search.SearchResult;

/**
 * Created by Roman on 12/12/2016.
 */

public class SearchResultAdapter extends CommonAdapter<SearchResult> {
    public SearchResultAdapter(Context context) {
        super(context);
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SearchResultHolder.newInstance(parent, inflater);
    }

    @Override
    public void onBindVHImpl(CommonViewHolder holder, int position) {
        if (holder instanceof SearchResultHolder) {
            SearchResult searchResult = data.get(position);
            ((SearchResultHolder) holder).title.setText(searchResult.title);
            ((SearchResultHolder) holder).author.setText(searchResult.author);
        }
    }

    private static class SearchResultHolder extends CommonAdapter.CommonViewHolder {
        public TextView title;
        public TextView author;

        public SearchResultHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
        }

        public static SearchResultHolder newInstance(ViewGroup parent, LayoutInflater inflater) {
            View itemView = inflater.inflate(R.layout.search_result_item, parent, false);
            return new SearchResultHolder(itemView);
        }
    }
}
