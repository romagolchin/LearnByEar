package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import olegkuro.learnbyear.auth.AuthenticationActivity;

/**
 * Created by Елена on 07.12.2016.
 */

public class SongActivity extends BaseActivity implements Button.OnClickListener {
    private class Tokenizer {
        public char[] splitChars = {' ', '\t', ',', '?', '!', ';', ':', '.',};
    }

    protected LinearLayout grammarSheet;
    protected TextView originalText;
    private final String TAG = getClass().getSimpleName();
    private static final int noSelection = -1;
    private List<String> lyrics = new ArrayList<>();
    private List<String> translation = new ArrayList<>();
    private List<String> all = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;

    private class Word {
        public String word;
        public List<String> meaning;
        public List<String> related;
        public int selectionIndex;
        public int tokenStart;
        public int tokenEnd;
    }

    private List<List<Word>> words;
    // get word by its start index
    private Map<WordCoordinates, Word> wordMap;
    private Map<String, WordCoordinates> coordiantesMap;
    // highlighting as well
    private List<Selection> selections;

    private class WordCoordinates {
        public int lineNumber;
        public int index;
    }

    private class Selection {
        public List<WordCoordinates> wordCoordinates;

    }


    private void changeSelection(int lineNumber, int index) {
        // if cursor position is not on a split character highlight the word and update
    }

    private void readTest(int resource) {
        try {
            InputStream is = getResources().openRawResource(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null) {
                switch (resource) {
                    case R.raw.translation:
                        translation.add(s);
                    default:
                        lyrics.add(s);
                }
            }

            is.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button: {
                if (!AuthenticationActivity.isSignedIn)
                    startActivity(new Intent(this, AuthenticationActivity.class));
                else {

                    //TODO edit
                    //set onClickListeners
                }
                break;
            }
            case R.id.submit_gloss_btn: {
                //TODO save to database
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                //here we'll add class lyrics
                //mFirebaseDatabaseReference.child("lyrics").setValue();
                break;
            }
            case R.id.preview_gloss_btn: {

                break;
            }
        }
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
        originalText = (TextView) findViewById(R.id.original_text);
        BottomSheetBehavior.from(grammarSheet).setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayNonEmptyData();
    }

    private void displayNonEmptyData() {
        readTest(R.raw.lyrics);
        readTest(R.raw.translation);
        for (int i = 0; i < Math.min(lyrics.size(), translation.size()); ++i) {
            all.add(lyrics.get(i));
            all.add(translation.get(i));
        }
        originalText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(grammarSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                int position = originalText.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                return true;
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
