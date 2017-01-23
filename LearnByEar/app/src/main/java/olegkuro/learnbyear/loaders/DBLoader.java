
package olegkuro.learnbyear.loaders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import olegkuro.learnbyear.loaders.search.SearchResult;
import olegkuro.learnbyear.model.Lyrics;
import olegkuro.learnbyear.model.UserEdit;

/**
 * Created by Елена on 27.12.2016.
 */

public class DBLoader {
    private final String TAG = "DBLoader";
    private List<String> langCodesTo;
    private static final String fullClassName = "olegkuro.learnbyear.loaders.DBLoader";
    public static final String searchInDBFinishedAction = fullClassName + ".DB_SEARCH_FINISHED";
    public static final String lyricsLoadedFromDBAction = fullClassName + ".DB_LYRICS_LOADED";

    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;


    public DBLoader(Context context) {
        this.context = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private Context context;

    public void search(final String request, final List<String> langCodesTo) {
        final List<SearchResult> searchResults = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //// FIXME: 28.12.2016
        final String[] tokens = request.toLowerCase().split("[^A-Za-z'-]");
        mDatabaseReference.child("index").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Map<String, ?> map = (Map<String, ?>) d.getValue();
                    String artist = ((String) map.get("artist"));
                    boolean artistContains = false;
                    for (String t : tokens) {
                        if (artist.toLowerCase().contains(t)) {
                            artistContains = true;
                            break;
                        }
                    }
                    String title = ((String) map.get("title"));
                    boolean titleContains = false;
                    for (String t : tokens) {
                        if (title.toLowerCase().contains(t)) {
                            titleContains = true;
                            break;
                        }

                    }
                    if (artistContains || titleContains) {
                        boolean translationLanguageAvailable = false;
                        @Nullable
                        Map<String, Boolean> translationLanguages = (Map<String, Boolean>) map.get("translationLanguages");
                        for (String langCode : langCodesTo)
                            if (translationLanguages != null && translationLanguages.containsKey(langCode)) {
                                translationLanguageAvailable = true;
                                break;
                            }
                        if (translationLanguageAvailable) {
                            SearchResult result = new SearchResult(title, artist, null);
                            result.setReference(mDatabaseReference.child("lyrics/" + d.getKey()));

                            searchResults.add(result);
                        }
                    }

                }
                Intent intent = new Intent(searchInDBFinishedAction);
                intent.putParcelableArrayListExtra("databaseResults", new ArrayList<>(searchResults));
                context.sendBroadcast(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.toException());
            }
        });
    }

    public SearchResult castLyricsToSearchRes(Lyrics lyrics, String translator) {
        SearchResult ret = new SearchResult(lyrics.title, lyrics.artist, null);
        ret.author = translator;
        ret.reference = FirebaseDatabase.getInstance().getReference().child("artists");
        return ret;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUser() {
        return mFirebaseUser.getDisplayName();
    }

    public void loadLyrics(DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserEdit userEdit = dataSnapshot.getValue(UserEdit.class);
                Log.d(TAG, userEdit.language + " "
                        + userEdit.translationLanguage + " " + userEdit.lyrics);
                Intent intent = new Intent(lyricsLoadedFromDBAction);
                intent.putExtra("userEdit", userEdit);
                context.sendBroadcast(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.toException());
            }
        });
    }

}
