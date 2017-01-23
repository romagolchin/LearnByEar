package olegkuro.learnbyear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import olegkuro.learnbyear.loaders.DBLoader;
import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchLoader;
import olegkuro.learnbyear.loaders.search.SearchResult;


/**
 * Created by Елена on 07.12.2016.
 */

public class SearchActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<SearchResult>>> {
    private BroadcastReceiver receiver;
    private boolean loaderInit = false;
    private Parcelable recyclerState;
    List<SearchResult> searchResults;
    Button searchButton;
    EditText searchField;
    private SearchResultAdapter adapter;
    RecyclerView recyclerSearchResults;
    private String request;
    private RecyclerView.LayoutManager layoutManager = null;
    private boolean loadedFromDb = false;
    private boolean loadedFromInternet = false;

    private void setVisibilityOnError() {
        recyclerSearchResults.setVisibility(View.GONE);
    }

    private void setVisibilityOnResult() {
        recyclerSearchResults.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private class ResultComparator implements Comparator<SearchResult> {
        @Override
        public int compare(SearchResult sr, SearchResult otherSr) {
            int res = sr.artist.compareTo(otherSr.artist);
            if (res == 0)
                res = sr.title.compareTo(otherSr.title);
            if (res == 0) {
                if (otherSr.reference == null && sr.reference != null)
                    res = 1;
                else if (otherSr.reference != null && sr.reference == null)
                    res = -1;
            }
            return res;
        }
    }

    private ArrayList<SearchResult> filterResults(List<SearchResult> searchResults) {
        Log.d(TAG, "before filter " + String.valueOf(searchResults.size()));
        List<SearchResult> resultsToFilter = new LinkedList<>(searchResults);
        SearchResult prev = null;
        ListIterator<SearchResult> iterator = resultsToFilter.listIterator();
        while (iterator.hasNext()) {
            SearchResult result = iterator.next();
            if (prev != null && prev.artist.equals(result.artist) && prev.title.equals(result.title))
                iterator.remove();
            prev = result;
        }
        Log.d(TAG, "after filter " + String.valueOf(resultsToFilter.size()));
        return new ArrayList<>(resultsToFilter);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        if (layoutManager == null)
            layoutManager = new LinearLayoutManager(this);
        if (adapter == null)
            adapter = new SearchResultAdapter(this);
        if (savedInstanceState != null) {
            loadedFromDb = (boolean) savedInstanceState.get("loadedFromDb");
            loadedFromInternet = (boolean) savedInstanceState.get("loadedFromInternet");
            request = (String) savedInstanceState.get("request");
            recyclerState = savedInstanceState.getParcelable("adapter");
            layoutManager.onRestoreInstanceState(recyclerState);
            loaderInit = savedInstanceState.getBoolean("loaderInit");
            try {
                searchResults =
                        savedInstanceState.getParcelableArrayList("searchResults");
                adapter.setData(searchResults);

            } catch (ClassCastException e) {
            }
            if (searchResults != null) {
                System.out.println(searchResults.size());
            }
        } else {
            searchResults = new ArrayList<>();
            adapter = new SearchResultAdapter(this);
        }
        recyclerSearchResults = (RecyclerView) findViewById(R.id.search_results);
        searchButton = (Button) findViewById(R.id.start_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchField = (EditText) findViewById(R.id.request);
                if (searchField.getText() == null ||
                        request != null && searchField.getText().toString().toLowerCase().equals(request.toLowerCase())) {
                    return;
                }
                request = searchField.getText().toString();
                if (request.length() == 0) {
                    showToast(getString(R.string.error_empty_request));
                    return;
                }
                adapter.clear();
                Bundle args = new Bundle();
                args.putString("request", request);
                args.putBoolean("loadedFromDb", loadedFromDb);
                args.putBoolean("loadedFromInternet", loadedFromInternet);
                if (!loaderInit) {
                    getSupportLoaderManager().initLoader(0, args, SearchActivity.this);
                    loaderInit = true;
                } else {
                    getSupportLoaderManager().restartLoader(0, args, SearchActivity.this);
                }
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<SearchResult> databaseResults = intent.getExtras().getParcelableArrayList("databaseResults");
                if (databaseResults != null)
                    searchResults.addAll(databaseResults);
                Collections.sort(searchResults, new ResultComparator());
                searchResults = filterResults(searchResults);
                loadedFromDb = true;
            }
        };
        recyclerSearchResults.setLayoutManager(layoutManager);
        recyclerSearchResults.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(DBLoader.searchInDBFinishedAction);
        registerReceiver(receiver, intentFilter);
        adapter.setListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Log.d(TAG + " onItemClick position", String.valueOf(position));
                SearchResult selectedResult = searchResults.get(position);
                Intent intent = new Intent(SearchActivity.this, SongActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                if (selectedResult != null) {
                    intent.putExtra("searchResult", selectedResult);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (layoutManager != null)
            outState.putParcelable("adapter", layoutManager.onSaveInstanceState());
        outState.putBoolean("loaderInit", loaderInit);
        outState.putParcelableArrayList("searchResults", new ArrayList<>(searchResults));
        outState.putBoolean("loadedFromDb", loadedFromDb);
        outState.putBoolean("loadedFromInternet", loadedFromInternet);
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
            if (searchResults == null) {
                searchResults = new ArrayList<>();
            }
            searchResults.addAll(result.data);
            Log.d(getClass().getSimpleName() + " getItemCount", String.valueOf(adapter.getItemCount()));
            Collections.sort(searchResults, new ResultComparator());
            searchResults = filterResults(searchResults);
            adapter.setData(searchResults);
            loadedFromInternet = true;
        } else {
            setVisibilityOnError();
            String errorMessage = "";
            switch (result.type) {
                case EMPTY:
                    errorMessage = "Search for " + request + " produced no results";
                    break;
                case NO_NETWORK:
                    errorMessage = "There is no network connection";
                    break;
                case UNKNOWN_ERROR:
                    errorMessage = "Unknown error";
                    break;
            }
            showToast(errorMessage);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<SearchResult>>> loader) {
        recyclerSearchResults.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop");
    }

}
