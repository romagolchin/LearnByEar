package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import olegkuro.learnbyear.loaders.DBLoader;
import olegkuro.learnbyear.loaders.HTMLLyricsParser;


/**
 * Created by Roman on 12/12/2016.
 */

public class SearchLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private LoadResult<List<SearchResult>> result;
    private final String TAG = getClass().getSimpleName();
    private String request;
    private List<String> langCodesTo;
    private boolean loadedFromDb;
    private boolean loadedFromInternet;


    public SearchLoader(Context context, Bundle args) {
        super(context);
        this.request = args.getString("request");
        this.langCodesTo = args.getStringArrayList("langCodesTo");
        loadedFromDb = args.getBoolean("loadedFromDb");
        loadedFromInternet = args.getBoolean("loadedFromInternet");
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        } else
            forceLoad();
    }

    @Override
    public LoadResult<List<SearchResult>> loadInBackground() {
        DBLoader dbLoader = new DBLoader(getContext());
        LoadResult<List<SearchResult>> searchRes =
                new LoadResult<>(null, LoadResult.ResultType.UNKNOWN_ERROR);
        // no need to return search results from DB
        // as they are read asynchronously and fetched to SearchActivity via intent
        if (!loadedFromDb)
            dbLoader.search(request, new ArrayList<>(Arrays.asList("ru")));
        Log.d("Using html", "PARSIM");
        if (!loadedFromInternet) {
            HTMLLyricsParser htmlLyricsParser = new HTMLLyricsParser();
            htmlLyricsParser.setContext(this.getContext());
            searchRes = htmlLyricsParser.search(request);
            Log.d(TAG, searchRes.type.toString());
        }
        return searchRes;
    }
}
