package olegkuro.learnbyear.loaders.lyrics;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.model.Lyrics;

/**
 * Created by Roman on 18/12/2016.
 * gets lyrics by url / database reference, now for testing lyrics are retrieved form raw files
 */

public class LyricsLoader extends AsyncTaskLoader<LoadResult<List<Lyrics>>> {
    public LyricsLoader(Context context) {
        super(context);
    }

    @Override
    public LoadResult<List<Lyrics>> loadInBackground() {
        return null;
    }
}
