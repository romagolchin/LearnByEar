package olegkuro.learnbyear;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    /**
     * here user can add and remove availableLanguages from list
     */

    public static final String spFileName = "olegkuro.learnbyear.LANGUAGES";

    public static Bundle availableLanguages;

    public static final String nativeLangGroup = "NATIVE";
    public static final String learnLangGroup = "LEARN";
    public static final String noGroup = "NONE";
    public static final String defaultLangKey = "DEFAULT";
    public static final String initLangKey = "INIT_LANG_PREFERENCES";


    BroadcastReceiver receiver;

    private String defaultCode;
    private Set<String> nativeLanguagesCodes;
    private Set<String> languagesToLearnCodes;
    private RecyclerView nativeLanguagesRecycler;
    private RecyclerView learnLanguagesRecycler;
    private TextView nativeLanguagesLabel;
    private TextView learnLanguagesLabel;

    private LanguageAdapter nativeLangAdapter;
    private LanguageAdapter learnLangAdapter;

    private ArrayAdapter<String> nativeAutoCompleteAdapter;
    private ArrayAdapter<String> learnAutoCompleteAdapter;

    private AutoCompleteTextView nativeLangInput;
    private AutoCompleteTextView learnLangInput;


    private SharedPreferences.Editor getEditor() {
        SharedPreferences langPreferences =
                getSharedPreferences(spFileName, MODE_PRIVATE);
        return langPreferences.edit();
    }

    /**
     * add not only to SP, also to local sets
     *
     * @param group    either native language or language to learn
     * @param langCode
     */
    public void addToSharedPreferences(String group, String langCode) {
        SharedPreferences langPreferences =
                getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = langPreferences.edit();
        editor.putString(langCode, group);
        editor.apply();
        if (nativeLangGroup.equals(group))
            nativeLanguagesCodes.add(langCode);
        else if (learnLangGroup.equals(group))
            languagesToLearnCodes.add(langCode);
    }

    public void removeFromSP(String group, String langCode) {
        SharedPreferences langPreferences =
                getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = langPreferences.edit();
        editor.putString(langCode, noGroup);
        editor.apply();
        if (nativeLangGroup.equals(group))
            nativeLanguagesCodes.remove(langCode);
        else if (learnLangGroup.equals(group))
            languagesToLearnCodes.remove(langCode);
    }

    public void setDefaultSP(String langCode) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(langCode, noGroup);
        editor.apply();
        defaultCode = langCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences langPreferences =
                getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = langPreferences.edit();

        nativeLanguagesCodes = new HashSet<>();
        languagesToLearnCodes = new HashSet<>();
        if (savedInstanceState != null) {
            availableLanguages = savedInstanceState.getParcelable("availableLanguages");
            if (savedInstanceState.getStringArrayList("nativeLanguagesCodes") != null) {
                nativeLanguagesCodes = new HashSet<>(savedInstanceState.getStringArrayList("nativeLanguagesCodes"));
            }
            if (savedInstanceState.getStringArrayList("languagesToLearnCodes") != null) {
                languagesToLearnCodes = new HashSet<>(savedInstanceState.getStringArrayList("languagesToLearnCodes"));
            }
        } else {
            availableLanguages = new Bundle();
            for (Locale locale : Locale.getAvailableLocales()) {
                availableLanguages.putString(locale.getDisplayLanguage(), locale.getLanguage());
            }
            if (langPreferences.getBoolean(initLangKey, false)) {
                for (Map.Entry<String, ?> entry : langPreferences.getAll().entrySet()) {
                    if (!entry.getKey().equals(initLangKey) && !entry.getKey().equals(defaultLangKey)) {
                        Object group = entry.getValue();
                        if (group.equals(nativeLangGroup))
                            nativeLanguagesCodes.add(entry.getKey());
                        if (group.equals(learnLangGroup))
                            languagesToLearnCodes.add(entry.getKey());
                    }
                }
            } else {
                setDefaultSP(Locale.getDefault().getLanguage());
                addToSharedPreferences(nativeLangGroup, Locale.getDefault().getLanguage());
                editor.putBoolean(initLangKey, true);
                editor.apply();
            }
        }
        setContentView(R.layout.activity_language);
        nativeLanguagesRecycler = (RecyclerView) findViewById(R.id.native_lang_list);
        registerForContextMenu(nativeLanguagesRecycler);
        learnLanguagesRecycler = (RecyclerView) findViewById(R.id.learn_lang_list);
        registerForContextMenu(learnLanguagesRecycler);
        nativeLanguagesLabel = (TextView) findViewById(R.id.native_lang_label);
        learnLanguagesLabel = (TextView) findViewById(R.id.learn_lang_label);

        nativeLangInput = (AutoCompleteTextView) findViewById(R.id.native_lang_input);
        nativeLangInput.setThreshold(3);
        nativeLanguagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        nativeLangAdapter = new LanguageAdapter(this);
        // FIXME: 22/01/2017 autocompletion not working
        nativeAutoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList<>(availableLanguages.keySet()));
        nativeLangInput.setAdapter(nativeAutoCompleteAdapter);
        nativeLanguagesRecycler.setAdapter(nativeLangAdapter);

        learnLangInput = (AutoCompleteTextView) findViewById(R.id.learn_lang_input);
        learnLangInput.setThreshold(3);
        learnLanguagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        learnLangAdapter = new LanguageAdapter(this);
        learnAutoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList<>(availableLanguages.keySet()));
        learnLangInput.setAdapter(learnAutoCompleteAdapter);
        learnLanguagesRecycler.setAdapter(learnLangAdapter);

        Button addNativeButton = (Button) findViewById(R.id.add_native_btn);
        addNativeButton.setOnClickListener(this);
        Button addLearnButton = (Button) findViewById(R.id.add_learn_btn);
        addLearnButton.setOnClickListener(this);

        nativeLangAdapter.setData(new ArrayList<>(nativeLanguagesCodes));
        learnLangAdapter.setData(new ArrayList<>(languagesToLearnCodes));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("availableLanguages", availableLanguages);
        outState.putStringArrayList("nativeLanguagesCodes", new ArrayList<>(nativeLanguagesCodes));
        outState.putStringArrayList("languagesToLearnCodes", new ArrayList<>(languagesToLearnCodes));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String codeToAdd = "en";
        if (id == R.id.add_learn_btn) {
            codeToAdd = availableLanguages.getString(learnLangInput.getText().toString());
        }
        if (id == R.id.add_native_btn)
            codeToAdd = availableLanguages.getString(nativeLangInput.getText().toString());
        boolean alreadyAdded = nativeLanguagesCodes.contains(codeToAdd)
                || languagesToLearnCodes.contains(codeToAdd);
        if (!alreadyAdded) {
            if (id == R.id.add_learn_btn) {
                learnLangAdapter.add(codeToAdd);
                addToSharedPreferences(learnLangGroup, codeToAdd);
            }
            if (id == R.id.add_native_btn) {
                nativeLangAdapter.add(codeToAdd);
                addToSharedPreferences(nativeLangGroup, codeToAdd);
            }
        }
    }
}
