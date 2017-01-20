package olegkuro.learnbyear.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import olegkuro.learnbyear.loaders.search.LoadResult;

/**
 * Created by Roman on 27/12/2016.
 */

public class Translator extends AsyncTaskLoader<LoadResult<String>> {
    private String langFrom;
    private String langTo;
    private String toTranslate;
    public Translator(Context context, String toTranslate) {
        super(context);
        this.toTranslate = toTranslate;
    }

    public void setLanguages(String langFrom, String langTo) {
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    @Override
    public LoadResult<String> loadInBackground() {
        return null;
    }
}
