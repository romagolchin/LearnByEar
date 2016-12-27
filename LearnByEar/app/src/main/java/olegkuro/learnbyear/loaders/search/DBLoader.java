
package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.os.Bundle;
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

import java.util.List;

import olegkuro.learnbyear.model.Lyrics;

/**
 * Created by Елена on 27.12.2016.
 */
/*
public class DBLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private static final String BASE_URI = "https://music.yandex.ru/handlers/music-search.jsx";
    private String request;
    private LoadResult<List<SearchResult>> result;
    private final String TAG = getClass().getSimpleName();
    private List<String> langCodesTo;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public DBLoader(Context context, Bundle args) {
        super(context);
        this.request = args.getString("request");
        this.langCodesTo = args.getStringArrayList("langCodesTo");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public LoadResult<List<SearchResult>> loadInBackground() {
        final LoadResult<List<SearchResult>> ret = null;
        Query query = databaseReference.orderByChild("db").equalTo(request);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Pair<Lyrics, String> pair = (Pair<Lyrics, String>) issue.getValue();
                        String translator = pair.second;
                        Lyrics lyrics = pair.first;
                    }
                }
                else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });


        return null;
    }

    public String getUser(){
        return firebaseUser.getDisplayName();
    }
}
*/