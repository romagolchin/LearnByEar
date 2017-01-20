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
import android.view.MotionEvent;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import olegkuro.learnbyear.auth.AuthenticationActivity;
import olegkuro.learnbyear.loaders.DBLoader;
import olegkuro.learnbyear.loaders.lyrics.LyricsLoader;
import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchResult;
import olegkuro.learnbyear.model.Lyrics;
import olegkuro.learnbyear.model.UserEdit;
import olegkuro.learnbyear.utils.CommonUtils;

import static olegkuro.learnbyear.loaders.search.LoadResult.ResultType.OK;

/**
 * Created by Елена on 07.12.2016.
 */

public class SongActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<UserEdit>>,
        Button.OnClickListener {
    private TextView artistAndTitle;
    protected LinearLayout grammarSheet;
    protected EditText originalText;
    protected EditText translatedText;

    // true if user changed lyrics, translation, or both
    private boolean madeChanges = false;
    private BroadcastReceiver receiver;
    // stores line beginnings in lyrics to determine line number by position in string
    // that contains whole lyrics
    private List<Integer> lineBeginnings = new ArrayList<>();
    // i maps to j when word that starts at position i ends at j
    private SparseArrayCompat<Integer> wordEnd = new SparseArrayCompat<>();
    private List<Integer> wordIndices = new ArrayList<>();
    private List<String> translationLines = new ArrayList<>();
    private List<String> originalLines;
    private Lyrics lyrics;
    private UserEdit userEdit;
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private SearchResult searchResult;

    private List<Selection> selections;
    private DBLoader dbLoader;

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

    private class Selection {

        public List<WordCoordinates> wordCoordinates;
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<UserEdit>> loader) {

    }


    @Override
    public void onLoadFinished(Loader<LoadResult<UserEdit>> loader, LoadResult<UserEdit> result) {
        if (result.type == OK) {
            userEdit = result.data;
            //todo: translate lyrics and update DB
            updateDB();
            originalLines = userEdit.lines;
            displayNonEmptyData();
            String delimiter = " |\\n";
            String[] splitLyrics = userEdit.lyrics.split("(?<=" + delimiter + ")|(?="
                    + delimiter + ")");
            int index = 0;
            for (int i = 0; i < splitLyrics.length; ++i) {
                String token = splitLyrics[i];
                Log.d(TAG + " token", token);
                if (!token.isEmpty() && !token.matches(delimiter)) {
                    wordEnd.put(index, index + token.length() - 1);
                    wordIndices.add(index);
                }
                index += token.length();
            }
            index = 0;
            for (String line : originalLines) {
                lineBeginnings.add(index);
                index += line.length();
            }
            Collections.sort(wordIndices);
            Collections.sort(lineBeginnings);
        }
    }

    @Override
    public Loader<LoadResult<UserEdit>> onCreateLoader(int id, Bundle args) {
        return new LyricsLoader(this, args);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_view_layout);
        Button editButton = (Button) findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "logged in");
        } else
            Log.d(TAG, "not logged in");
        grammarSheet = (LinearLayout) findViewById(R.id.grammar_sheet);
        originalText = (EditText) findViewById(R.id.original_text);
        translatedText = (EditText) findViewById(R.id.translated_text);
        BottomSheetBehavior.from(grammarSheet).setState(BottomSheetBehavior.STATE_HIDDEN);
        Bundle args = new Bundle();
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            madeChanges = savedInstanceState.getBoolean("MADE_CHANGES");
            lineBeginnings = savedInstanceState.getIntegerArrayList("LINE_BEGINNINGS");
            //FIXME 
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
            // already translated
            dbLoader.loadLyrics(searchResult.reference);
        } else {
            args.putString("url", searchResult.url.toString());
            getSupportLoaderManager().initLoader(0, args, this);
        }
        lyrics = new Lyrics(searchResult.artist, searchResult.title,
                new UserEdit(null, searchResult.translationType,
                searchResult.langTo, null, null, null, null, searchResult.langFrom));
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DBLoader.lyricsLoadedFromDBAction.equals(intent.getAction())) {
                    userEdit = (UserEdit) intent.getSerializableExtra("userEdit");

                }
            }
        };
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
        switch (view.getId()) {
            case R.id.edit_button: {
                if (mAuth.getCurrentUser() == null)
                    startActivity(new Intent(this, AuthenticationActivity.class));
                else {

                    //TODO edit
                    //set onClickListeners
                }
                break;
            }
            case R.id.submit_gloss_btn: {
                //TODO save to database
                //here we'll add class songText
                updateDB();
                break;
            }
            case R.id.preview_gloss_btn: {

                break;
            }
        }
    }

    private void displayNonEmptyData() {
        originalText.setText(userEdit.lyrics);
        originalText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(grammarSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Log.d(TAG + " state", "" + behavior.getState());
                int position = originalText.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                WordCoordinates coordinates = getWordCoordinatesByPosition(position);
                Log.d(TAG + " coordinates", coordinates.lineNumber + " " +
                        coordinates.wordIndex);
                if (coordinates.valid()) {
                    String translationLine = null;
                    try {
                        translationLine = translationLines.get(coordinates.lineNumber);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    translatedText.setText(translationLine);

                }
                return true;
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("MADE_CHANGES", madeChanges);
        outState.putIntegerArrayList("LINE_BEGINNINGS", new ArrayList<>(lineBeginnings));
        //FIXME 
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
        // TODO languagesTo
        String pushId;
        if (!madeChanges) {
            // FIXME: 19/01/2017 ref == null does not yield that lyrics are not in DB
            // if lyrics are in DB already
            if (searchResult.reference != null || searchResult.presentInDB)
                return;
            DatabaseReference newIndexReference = index.push();
            pushId = newIndexReference.getKey();
            newIndexReference.setValue(indexElement);
            // for debug only
            userEdit.translationLanguage = "ru";
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
        // TODO: 19/01/2017 update this when lyrics from DB are loaded 
        searchResult.presentInDB = true;
        edits.push().setValue(userEdit);
    }


}
