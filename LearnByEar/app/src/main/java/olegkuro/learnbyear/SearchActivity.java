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

import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchLoader;
import olegkuro.learnbyear.loaders.search.SearchResult;

/**
 * Created by Елена on 07.12.2016.
 */

public class SearchActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<SearchResult>>> {

    private Parcelable recyclerState;
    List<SearchResult> data;
    Button upButton;
    Button searchButton;
    EditText searchField;
    private SearchResultAdapter adapter;
    TextView error;
    RecyclerView searchResults;
    private String request;
    private RecyclerView.LayoutManager layoutManager = null;


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
        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        setContentView(R.layout.searchlist);
        error = (TextView) findViewById(R.id.search_error);
        searchResults = (RecyclerView) findViewById(R.id.search_results);
        layoutManager = searchResults.getLayoutManager();
        searchButton = (Button) findViewById(R.id.start_search);
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
                getSupportLoaderManager().initLoader(0, args, SearchActivity.this);
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
        if (layoutManager != null)
            outState.putParcelable("adapter", layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable("adapter");
            if (layoutManager != null)
                layoutManager.onRestoreInstanceState(recyclerState);
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
            adapter.setData(result.data);
//            data = result.data;
            searchResults.setAdapter(adapter);
            adapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int lineNumber, int index) {

                }

                @Override
                public void onItemClick(int position) {
                    URL url = result.data.get(position).url;
                    startActivity(new Intent(SearchActivity.this, SongActivity.class).putExtra("url", url));
                }
            });

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
