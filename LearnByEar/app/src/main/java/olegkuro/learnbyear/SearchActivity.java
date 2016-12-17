package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import olegkuro.learnbyear.loader.LoadResult;
import olegkuro.learnbyear.loader.SearchLoader;
import olegkuro.learnbyear.loader.SearchResult;

/**
 * Created by Елена on 07.12.2016.
 */

public class SearchActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<SearchResult>>> {

    private Parcelable recyclerState;
    List<SearchResult> data;
    Button upButton;
    @BindView(R.id.start_search) Button searchButton;
    EditText searchField;
    private SearchResultAdapter adapter;
    @BindView(R.id.search_error) protected TextView error;
    @BindView(R.id.search_results) protected RecyclerView searchResults;
    private String request;


    private void setVisibilityOnError() {
        error.setVisibility(View.VISIBLE);
        searchResults.setVisibility(View.GONE);
    }

    private void setVisibilityOnResult() {
        error.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchField = (EditText) findViewById(R.id.request);
                request = searchField.getText().toString();
                if (request.length() == 0) {
                    setVisibilityOnError();
                    error.setText(getString(R.string.error_empty_request));
                    return;
                }
                Bundle args = new Bundle();
                args.putString("request", request);
                getSupportLoaderManager().initLoader(0, args, null);
            }
        });

        //simply turns back to the first item
        upButton = (Button) findViewById(R.id.button_up);
        upButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchResults.scrollToPosition(0);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // only UI is saved, whereas data is saved in Loader
        super.onSaveInstanceState(outState);
        outState.putParcelable("adapter", searchResults.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable("adapter");
            searchResults.getLayoutManager().onRestoreInstanceState(recyclerState);
        }
    }

    @Override
    public Loader<LoadResult<List<SearchResult>>> onCreateLoader(int id, Bundle args) {
        return new SearchLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<SearchResult>>> loader,
                               final LoadResult<List<SearchResult>> result) {
        if (result.type == LoadResult.ResultType.OK) {
            setVisibilityOnResult();
            adapter = new SearchResultAdapter(this);
            adapter.setSearchResults(result.data);
            data = result.data;
            adapter.setListener(new SearchResultAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    URL url = result.data.get(position).url;
                    startActivity(new Intent(SearchActivity.this, SongActivity.class).putExtra("url", url));
                }
            });
            searchResults.setAdapter(adapter);

        } else {
            setVisibilityOnError();
            String errorMessage = "";
            switch (result.type) {
                case EMPTY:
                    errorMessage = "Search for" + request + "produced no results";
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
        error.setVisibility(View.GONE);
        searchResults.setVisibility(View.GONE);
    }

}
