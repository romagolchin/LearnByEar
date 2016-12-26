package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import olegkuro.learnbyear.loaders.HTMLLyricsParser;

/**
 * Created by Roman on 12/12/2016.
 */

public class SearchLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private LoadResult<List<SearchResult>> result;
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "https://music.yandex.ru/handlers/music-search.jsx";
    private String request;
    private List<String> langCodesTo;

    public SearchLoader(Context context, Bundle args) {
        super(context);
        this.request = args.getString("request");
        this.langCodesTo = args.getStringArrayList("langCodesTo");
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
        HTMLLyricsParser parser = new HTMLLyricsParser();
        parser.setContext(getContext());
        return parser.search(request);
    }
}
