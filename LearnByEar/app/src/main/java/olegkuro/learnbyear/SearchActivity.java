package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
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
    private boolean loaderInit = false;
    private Parcelable recyclerState;
    List<SearchResult> data;
    Button upButton;
    Button searchButton;
    EditText searchField;
    private SearchResultAdapter adapter;
    RecyclerView searchResults;
    private String request;
    private RecyclerView.LayoutManager layoutManager = null;
    private LinearLayoutManager manager;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private void setVisibilityOnError() {
        searchResults.setVisibility(View.GONE);
    }

    private void setVisibilityOnResult() {
        searchResults.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        setContentView(R.layout.searchlist);
        searchResults = (RecyclerView) findViewById(R.id.search_results);
        searchButton = (Button) findViewById(R.id.start_search);
        adapter = new SearchResultAdapter(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchField = (EditText) findViewById(R.id.request);
                if (searchField.getText() != null && searchField.getText().toString().equals(request)) {
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
                if (!loaderInit) {
                    getSupportLoaderManager().initLoader(0, args, SearchActivity.this);
                    loaderInit = true;
                } else {
                    getSupportLoaderManager().restartLoader(0, args, SearchActivity.this);
                }
            }
        });
        upButton = (Button) findViewById(R.id.button_up);
        //simply turns back to the first item
        upButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("test","scrolling up");
                //databaseReference = FirebaseDatabase.getInstance().getReference();
                //Log.d("test", "database connected");
                //databaseReference.child("lyrics").setValue(new Lyrics("english", "ya lublu sobak", "MC Panin"));
                //Toast toast = Toast.makeText(getApplicationContext(), R.string.thanks_for_appending, Toast.LENGTH_LONG);
                //toast.setGravity(Gravity.CENTER, 0, 0);
                //toast.show();

                searchResults.scrollToPosition(0);
            }
        });

        searchResults.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = searchResults.getLayoutManager();
        searchResults.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // only UI is saved, whereas data is saved in Loader
        super.onSaveInstanceState(outState);
        if (layoutManager != null)
            outState.putParcelable("adapter", layoutManager.onSaveInstanceState());
        outState.putBoolean("loaderInit", loaderInit);
        outState.putSerializable("data", (Serializable) data);
    }

    @Override
    protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable("adapter");
            if (layoutManager != null)
                layoutManager.onRestoreInstanceState(recyclerState);
            loaderInit = savedInstanceState.getBoolean("loaderInit");
            try {
                data = (List<SearchResult>) savedInstanceState.getSerializable("data");
                adapter.setData(data);
            } catch(ClassCastException e) {}
            if (data != null)
                System.out.println(data.size());
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
            adapter.setData(result.data);
            data = result.data;
            Log.d(getClass().getSimpleName() + " getItemCount", String.valueOf(adapter.getItemCount()));
            adapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int lineNumber, int index) {

                }
                @Override
                public void onItemClick(int position) {
                    Log.d(TAG + " onItemClick position", String.valueOf(position));
                    SearchResult posResult = result.data.get(position);
                    Intent intent = new Intent(SearchActivity.this, SongActivity.class);
                    if (posResult != null) {
                        if (posResult.reference != null)
                            intent.putExtra("reference", (Serializable) posResult.reference);
                    }
                    startActivity(intent);
                }
            });
        upButton.bringToFront();
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
        searchResults.setVisibility(View.GONE);
    }

    /*
    private void testResult(){
        if (data.isEmpty()){
            for (int i = 1; i < 20; i++) {
                try {
                    data.add(new SearchResult("Animals", "Animals", new URL("http://lyricstranslate.com/en/animals-%D0%B6%D0%B8%D0%B2%D0%BE%D1%82%D0%BD%D1%8B%D0%B5.html-0")));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                setVisibilityOnResult();
                adapter = new SearchResultAdapter(this);
                adapter.setData(data);
                searchResults.setAdapter(adapter);
                adapter.setListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int lineNumber, int index) {

                    }

                    @Override
                    public void onItemClick(int position) {
                        URL url = data.get(position).url;
                        startActivity(new Intent(SearchActivity.this, SongActivity.class).putExtra("url", url));
                    }
                });
            }
        }
        upButton.bringToFront();
    }
    */
}
