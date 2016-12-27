package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import olegkuro.learnbyear.loaders.HTMLLyricsParser;
import olegkuro.learnbyear.model.Lyrics;

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

/*
    automaticly appends firebase
 */
    public void appendDB(LoadResult<List<SearchResult>> val){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null)
            Log.d(TAG, firebaseUser.getDisplayName());

        if(val.data.isEmpty())
            return;
        else {
            for (SearchResult song : val.data ){
                Lyrics lyrics = new Lyrics("eng", song.artist, null, song.title, null, null, null, Lyrics.TranslationType.MACHINE);
                Pair <Lyrics, String> app;
                if (firebaseUser != null)
                    app = new Pair<>(lyrics, firebaseUser.getDisplayName());
                else
                    app = new Pair<>(lyrics, "ANONYMOUS");
                Log.d(TAG, app.toString());
                mDatabase.child("db").child(lyrics.artist).child(lyrics.title).setValue(app);
            }
        }
    }

    @Override
    public LoadResult<List<SearchResult>> loadInBackground() {
        HTMLLyricsParser htmlLyricsParser = new HTMLLyricsParser();
        htmlLyricsParser.setContext(this.getContext());
        LoadResult<List<SearchResult>> searchRes = htmlLyricsParser.search(request);
        appendDB(searchRes);
        return searchRes;
    }
}
