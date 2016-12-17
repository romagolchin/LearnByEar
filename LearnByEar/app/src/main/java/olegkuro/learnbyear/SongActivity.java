package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

/**
 * Created by Елена on 07.12.2016.
 */

public class SongActivity extends BaseActivity implements Button.OnClickListener {
    private class Tokenizer {
        public char[] splitChars = {' ', '\t', ',', '?', '!', ';', ':', '.', };
    }
    private final String TAG = getClass().getSimpleName();
    private static final int noSelection = -1;
    private List<String> lyrics;
    private List<String> translations;
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

    /**
     * if the word is selected, this action overwrites that selection
     */
    private void changeSelection(int lineNumber, int index) {
        // if cursor position is not on a split character highlight the word and update
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

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
