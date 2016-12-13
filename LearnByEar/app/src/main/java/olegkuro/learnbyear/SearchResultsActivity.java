package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import olegkuro.learnbyear.loader.LoadResult;
import olegkuro.learnbyear.loader.LyricsLoader;
import olegkuro.learnbyear.loader.SearchResult;

public class SearchResultsActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<SearchResult>>> {


    private SearchResultAdapter adapter;
    @BindView(R.id.search_error) protected TextView error;
    @BindView(R.id.search_results) protected RecyclerView searchResults;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle args = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, args, null);
        query = args.getString("query");
    }

    @Override
    public Loader<LoadResult<List<SearchResult>>> onCreateLoader(int id, Bundle args) {
        return new LyricsLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<SearchResult>>> loader, final LoadResult<List<SearchResult>> result) {
        if (result.type == LoadResult.ResultType.OK) {
            error.setVisibility(View.GONE);
            searchResults.setVisibility(View.VISIBLE);
            adapter = new SearchResultAdapter(this);
            adapter.setSearchResults(result.data);
            adapter.setListener(new SearchResultAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    URL url = result.data.get(position).url;
                    startActivity(new Intent(SearchResultsActivity.this, SongActivity.class).putExtra("url", url));
                }
            });
            searchResults.setAdapter(adapter);

        } else {
            error.setVisibility(View.VISIBLE);
            searchResults.setVisibility(View.GONE);
            String errorMessage = "";
            switch (result.type) {
                case EMPTY:
                    errorMessage = "Search for" + query + "produced no results";
                    break;
                case NO_NETWORK:
                    errorMessage = "There is no network connection";
                    break;
                case UNKNOWN_ERROR:
                    errorMessage = "Unknown error";
                    break;
            }
            error.setText(errorMessage);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<SearchResult>>> loader) {

    }
}
