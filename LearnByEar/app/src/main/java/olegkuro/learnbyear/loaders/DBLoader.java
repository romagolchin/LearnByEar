
package olegkuro.learnbyear.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import olegkuro.learnbyear.loaders.search.*;
import olegkuro.learnbyear.model.Lyrics;

/**
 * Created by Елена on 27.12.2016.
 */

public class DBLoader {
    private String request;
    private LoadResult<List<SearchResult>> result;
    private final String TAG = getClass().getSimpleName();
    private List<String> langCodesTo;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;


    @NonNull
    public LoadResult<List<SearchResult>> search(String request){
        LoadResult.ResultType type = LoadResult.ResultType.UNKNOWN_ERROR;
        final List<SearchResult> searchResults = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query queryArtist =databaseReference.orderByChild("db").startAt(request);
        Query queryTitle = databaseReference.orderByChild("db").startAt(request);

        queryArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Pair<Lyrics, String> pair = (Pair<Lyrics, String>) d.getValue();
                        String translator = pair.second;
                        Lyrics lyrics = pair.first;
                        searchResults.add(castLyricsToSearchRes(lyrics,translator));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.toException());
            }
        });
        if (!searchResults.isEmpty())
            return new LoadResult<>(searchResults, LoadResult.ResultType.OK);

        queryTitle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Pair<Lyrics, String> pair = (Pair<Lyrics, String>) d.getValue();
                        String translator = pair.second;
                        Lyrics lyrics = pair.first;
                        searchResults.add(castLyricsToSearchRes(lyrics,translator));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.toException());
            }
        });
        if (!searchResults.isEmpty())
            return new LoadResult<>(searchResults, LoadResult.ResultType.OK);
        else
            // nothing found in db
            return new LoadResult<>(null, LoadResult.ResultType.EMPTY);
    }

    public SearchResult castLyricsToSearchRes(Lyrics lyrics, String translator){
        SearchResult ret = new SearchResult(lyrics.title, lyrics.artist, null);
        ret.author = translator;
        ret.reference = FirebaseDatabase.getInstance().getReference().child("db");
        return ret;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    Context context;
    public String getUser(){
        return firebaseUser.getDisplayName();
    }
}
