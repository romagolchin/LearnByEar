package olegkuro.learnbyear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import olegkuro.learnbyear.loaders.DBLoader;
import olegkuro.learnbyear.loaders.TranslationLoader;
import olegkuro.learnbyear.loaders.lyrics.LyricsLoader;
import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchResult;
import olegkuro.learnbyear.model.Lyrics;
import olegkuro.learnbyear.model.UserEdit;
import olegkuro.learnbyear.utils.CommonUtils;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static olegkuro.learnbyear.loaders.search.LoadResult.ResultType.OK;

/**
 * Created by Елена on 07.12.2016.
 */

public class SongActivity extends BaseActivity
        implements Button.OnClickListener {
    private TextView artistAndTitle;
    protected LinearLayout translationSheet;
    protected EditText originalText;
    protected EditText translatedText;

    // true if user changed lyrics, translation, or both
    private boolean madeChanges = false;
    private BroadcastReceiver receiver;
    // stores line beginnings in lyrics to determine line number by position in string
    // that contains whole lyrics
    private List<Integer> lineBeginnings = new ArrayList<>();
    private List<Integer> translationLineBeginnings = new ArrayList<>();

    // i maps to j when word that starts at position i ends at j
    private SparseArrayCompat<Integer> wordEnd = new SparseArrayCompat<>();
    private List<Integer> wordIndices = new ArrayList<>();
    private List<String> translationLines = new ArrayList<>();
    private List<String> originalLines;
    private UserEdit userEdit;
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private SearchResult searchResult;
    private Integer lyricsLineNumber;
    private DBLoader dbLoader;

    private List<Integer> getLineBeginnings(List<String> lines) {
        List<Integer> result = new ArrayList<>();
        int index = 0;
        for (String line : lines)
            if (!line.isEmpty() && !"\n".equals(line)) {
                result.add(index);
                index += line.length();
            }
        return result;
    }

    private LoaderManager.LoaderCallbacks<LoadResult<UserEdit>> mLyricsLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<LoadResult<UserEdit>>() {
                @Override
                public Loader<LoadResult<UserEdit>> onCreateLoader(int id, Bundle args) {
                    return new LyricsLoader(SongActivity.this, args);
                }

                @Override
                public void onLoadFinished(Loader<LoadResult<UserEdit>> loader, LoadResult<UserEdit> result
                ) {
                    if (result.type == OK) {
                        userEdit = result.data;
                        originalLines = userEdit.lines;
                        displayNonEmptyData();
                        lineBeginnings = getLineBeginnings(originalLines);
                        Collections.sort(lineBeginnings);
                    }
                }

                @Override
                public void onLoaderReset(Loader<LoadResult<UserEdit>> loader) {

                }
            };
    private LoaderManager.LoaderCallbacks<LoadResult<String>> mTranslationCallbacks = new LoaderManager.LoaderCallbacks<LoadResult<String>>() {
        @Override
        public Loader<LoadResult<String>> onCreateLoader(int id, Bundle args) {
            return new TranslationLoader(SongActivity.this, userEdit.lyrics.replaceAll("\\n", "\r\n"),
                    userEdit.translationLanguage);
        }

        @Override
        public void onLoadFinished(Loader<LoadResult<String>> loader, LoadResult<String> data) {
            if (data.type == OK) {
                String translation = data.data;
                // exclude XML tag
                int begin = translation.indexOf('>') + 1;
                int end = translation.lastIndexOf('<') - 1;
                if (end >= begin)
                    translation = translation.substring(begin, end);
                userEdit.translatedText = translation;
                translationLines = new ArrayList<>(Arrays.asList(translation.split("\\r\\n")));
                translationLineBeginnings = getLineBeginnings(translationLines);
                showTranslation();
            }
        }

        @Override
        public void onLoaderReset(Loader<LoadResult<String>> loader) {

        }
    };

    static class BundleSparseIntArrayConverter {

        public static Bundle toBundle(SparseArrayCompat<Integer> arrayCompat) {
            Bundle bundle = new Bundle();
            for (int i = 0; i < arrayCompat.size(); ++i) {
                bundle.putInt(String.valueOf(arrayCompat.keyAt(i)), arrayCompat.valueAt(i));
            }
            return bundle;
        }

        public static SparseArrayCompat<Integer> toSparseIntArray(Bundle bundle) {
            SparseArrayCompat<Integer> arrayCompat = new SparseArrayCompat<>();
            for (String k : bundle.keySet())
                arrayCompat.put(Integer.valueOf(k), bundle.getInt(k));
            return arrayCompat;
        }
    }

    private class WordCoordinates {
        public int lineNumber;
        // position of the word in wordIndices
        // use to determine position of corresponding selection
        // in selections
        public int wordIndex;

        public WordCoordinates(int lineNumber, int wordIndex) {
            this.lineNumber = lineNumber;
            this.wordIndex = wordIndex;
        }

        public boolean valid() {
            return lineNumber >= 0 && lineNumber < originalLines.size()
                    && wordIndex >= 0 && wordIndex < wordIndices.size();
        }
    }

    private void getTranslation() {
        if ((userEdit.translatedText == null || userEdit.translatedText.isEmpty()) &&
                userEdit.translationLanguage != null)
            getSupportLoaderManager().initLoader(1, null, mTranslationCallbacks);
    }

    private void showTranslation() {
        updateDB();
        translatedText.setText(userEdit.translatedText);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbLoader = new DBLoader(this);
        setContentView(R.layout.song_view_layout);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "logged in");
        } else
            Log.d(TAG, "not logged in");
        originalText = (EditText) findViewById(R.id.original_text);
        translatedText = (EditText) findViewById(R.id.translated_text);
        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            translationSheet = (LinearLayout) findViewById(R.id.translation_sheet);
            BottomSheetBehavior.from(translationSheet).setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        Bundle args = new Bundle();
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            madeChanges = savedInstanceState.getBoolean("MADE_CHANGES");
            lineBeginnings = savedInstanceState.getIntegerArrayList("LINE_BEGINNINGS");
            wordEnd = BundleSparseIntArrayConverter.toSparseIntArray((Bundle) savedInstanceState.getParcelable("WORD_END"));

            wordIndices = savedInstanceState.getIntegerArrayList("WORD_INDICES");
            originalLines = savedInstanceState.getStringArrayList("ORIGINAL_LINES");
            translationLines = savedInstanceState.getStringArrayList("TRANSLATION_LINES");
            userEdit = (UserEdit) savedInstanceState.getSerializable("USER_EDIT");
            searchResult = savedInstanceState.getParcelable("SEARCH_RESULT");
        }
        if (searchResult == null) {
            searchResult = intent.getParcelableExtra("searchResult");
        }
        artistAndTitle = (TextView) findViewById(R.id.artist_and_title);
        artistAndTitle.setText(searchResult.artist + CommonUtils.longDash + searchResult.title);
        if (searchResult.reference != null) {
            // already in DB
            dbLoader.loadLyrics(searchResult.reference);
        } else {
            args.putString("url", searchResult.url.toString());

            getSupportLoaderManager().initLoader(0, args, mLyricsLoaderCallbacks);
        }
        if (userEdit == null)
            userEdit = new UserEdit();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DBLoader.lyricsLoadedFromDBAction.equals(intent.getAction())) {
                    userEdit = (UserEdit) intent.getSerializableExtra("userEdit");

                }
            }
        };
        userEdit.translationLanguage = Locale.getDefault().getLanguage();
        getTranslation();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter());
        mAuth = FirebaseAuth.getInstance();
        toggleEditability(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void toggleEditability(boolean editable) {
        originalText.setFocusable(editable);
        originalText.setClickable(true);
        translatedText.setFocusable(editable);
        translatedText.setClickable(true);

    }

    private int lastNotGreaterThan(List<Integer> list, int x) {
        int insertPos = Collections.binarySearch(list, x);
        if (insertPos >= 0) {
            return insertPos;
        } else {
            return -insertPos - 2;
        }
    }

    private WordCoordinates getWordCoordinatesByPosition(int position) {
        int lineNumber = lastNotGreaterThan(lineBeginnings, position);
        int wordIndex;
        wordIndex = lastNotGreaterThan(wordIndices, position);
        try {
            if (wordEnd.get(wordIndices.get(wordIndex)) < position)
                wordIndex = -1;
        } catch (IndexOutOfBoundsException e) {
            wordIndex = -1;
        }
        return new WordCoordinates(lineNumber, wordIndex);
    }

    @Override
    public void onClick(View view) {

    }

    private void displayNonEmptyData() {
        originalText.setText(userEdit.lyrics);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("MADE_CHANGES", madeChanges);
        outState.putIntegerArrayList("LINE_BEGINNINGS", new ArrayList<>(lineBeginnings));
        outState.putParcelable("WORD_END", (BundleSparseIntArrayConverter.toBundle(wordEnd)));

        outState.putIntegerArrayList("WORD_INDICES", new ArrayList<>(wordIndices));
        outState.putStringArrayList("ORIGINAL_LINES", new ArrayList<>(originalLines));
        outState.putStringArrayList("TRANSLATION_LINES", new ArrayList<>(translationLines));
        outState.putSerializable("USER_EDIT", userEdit);
        outState.putParcelable("SEARCH_RESULT", searchResult);
        super.onSaveInstanceState(outState);
    }

    private void updateDB() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userName = null;
        if (firebaseUser != null) {
            userName = firebaseUser.getDisplayName();
            Log.d(TAG, userName);
        }
        DatabaseReference index = mDatabaseReference.child("index");
        DatabaseReference edits;
        Map<String, String> indexElement = new HashMap<>();
        indexElement.put("artist", searchResult.artist);
        indexElement.put("title", searchResult.title);
        String pushId;
        if (!madeChanges) {
            // if lyrics are in DB already
            if (searchResult.reference != null)
                return;
            DatabaseReference newIndexReference = index.push();
            pushId = newIndexReference.getKey();
            newIndexReference.setValue(indexElement);
            // for debug only
            if (userEdit.translationLanguage != null)
                newIndexReference.child("translationLanguages").child(userEdit.translationLanguage).setValue(true);
            edits = mDatabaseReference.child("lyrics/" + pushId + "/edits/second");
        } else {
            // REMOVE AFTER TESTING !!!
            assert searchResult.reference != null;
            if (firebaseUser != null) {
                userEdit.user = userName;
                userEdit.typeOfTranslation = Lyrics.HUMAN;
            }
            edits = searchResult.reference.child("edits/first");
        }
        searchResult.presentInDB = true;
        edits.push().setValue(userEdit);
    }


}
