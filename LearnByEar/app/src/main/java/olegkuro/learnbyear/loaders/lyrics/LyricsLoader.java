package olegkuro.learnbyear.loaders.lyrics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import olegkuro.learnbyear.loaders.HTMLLyricsParser;
import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.model.UserEdit;

/**
 * Created by Roman on 18/12/2016.
 */

public class LyricsLoader extends AsyncTaskLoader<LoadResult<UserEdit>> {
    private String url;

    public LyricsLoader(Context context, Bundle args) {
        super(context);
        url = args.getString("url");
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<UserEdit> loadInBackground() {
        HTMLLyricsParser parser = new HTMLLyricsParser();
        parser.setContext(getContext());
        return parser.parse(url);
    }
}
