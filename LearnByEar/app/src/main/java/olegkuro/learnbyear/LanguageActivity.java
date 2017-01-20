package olegkuro.learnbyear;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    /**
     * here user can add and remove languages from list
     */

    public static Bundle LANGUAGES;

    private RecyclerView nativeLanguagesRecycler;
    private RecyclerView learnLanguagesRecycler;
    private TextView nativeLanguagesLabel;
    private TextView learnLanguagesLabel;

    private LanguageAdapter nativeLangAdapter;
    private LanguageAdapter learnLangAdapter;

    private ArrayAdapter<String> nativeAutoCompleteAdapter;
    private ArrayAdapter<String> learnAutoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            LANGUAGES = savedInstanceState.getParcelable("LANGUAGES");
        }
        if (LANGUAGES == null) {
            LANGUAGES = new Bundle();
            for (Locale locale : Locale.getAvailableLocales()) {
                LANGUAGES.putString(locale.getDisplayLanguage(), locale.getISO3Language());
            }
        }
        setContentView(R.layout.activity_language);
        nativeLanguagesRecycler = (RecyclerView) findViewById(R.id.native_lang_list);
        learnLanguagesRecycler = (RecyclerView) findViewById(R.id.learn_lang_list);
        nativeLanguagesLabel = (TextView) findViewById(R.id.native_lang_label);
        learnLanguagesLabel = (TextView) findViewById(R.id.learn_lang_label);

        AutoCompleteTextView nativeLangInput = (AutoCompleteTextView) findViewById(R.id.native_lang_input);
        nativeLangInput.setThreshold(3);
        nativeLanguagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        nativeAutoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList<>(LANGUAGES.keySet()));
        nativeLanguagesRecycler.setAdapter(nativeLangAdapter);

        AutoCompleteTextView learnLangInput = (AutoCompleteTextView) findViewById(R.id.learn_lang_input);
        learnLangInput.setThreshold(3);
        learnLanguagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        learnLangAdapter = new LanguageAdapter(this);
        learnAutoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList<>(LANGUAGES.keySet()));
        learnLanguagesRecycler.setAdapter(learnLangAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("LANGUAGES", LANGUAGES);
    }
}
