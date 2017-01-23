package olegkuro.learnbyear;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import olegkuro.learnbyear.loaders.HTMLLyricsParser;
import olegkuro.learnbyear.loaders.TranslationLoader;
import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchResult;
import olegkuro.learnbyear.model.Lyrics;
import olegkuro.learnbyear.model.UserEdit;
import olegkuro.learnbyear.utils.CommonUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = getTargetContext();

        assertEquals("olegkuro.learnbyear", appContext.getPackageName());
    }

    @Test
    public void testHTMLParse() throws Exception {
        HTMLLyricsParser parser = new HTMLLyricsParser();
        parser.setContext(getTargetContext());
        LoadResult<List<SearchResult>> loadResult = parser.search("bob dylan");
        for (SearchResult searchResult : loadResult.data)
            Log.d(getClass().getSimpleName(), searchResult.title);
        LoadResult<UserEdit> lyrics = parser.parse(loadResult.data.get(0).url.toString());
        Log.d(getClass().getSimpleName(), lyrics.data.lyrics);
    }

    @Test
    public void checkRegex() throws Exception {
        String str = "]{Madam, I'm Adam!! ?-";
        String delimiter = "[^a-zA-Z'-]";
        String[] splitStr = str.split("(?<=" + delimiter + ")|(?="
                + delimiter + ")");
        for (int i = 0; i  < splitStr.length; ++i)
            Log.d("checkRegex", splitStr[i]);
    }

    public void databaseInsertHelper(final Lyrics lyrics) throws Exception {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference artists = reference.child("artists");
//        DatabaseReference newEdit = artists.child(lyrics.artist).child(lyrics.title).push();
        DatabaseReference newEdit = artists.child("abacaba");
        newEdit.setValue("cabaaba");
    }

    @Test
    public void testSearch() throws Exception {
        DatabaseReference artists = FirebaseDatabase.getInstance().getReference().child("artists");
    }

    @Test
    public void testDB() throws Exception {
        String translation, lyrics;
        Context context = InstrumentationRegistry.getTargetContext();
        InputStream is = context.getResources().openRawResource(R.raw.translation);
        translation = CommonUtils.readToString(is);
        is.close();
        is = context.getResources().openRawResource(R.raw.lyrics);
        lyrics = CommonUtils.readToString(is);
        is.close();
        List<String> lines = new ArrayList<>(Arrays.asList(lyrics.split("\\n")));
        String query = "A1";
        DatabaseReference artists = FirebaseDatabase.getInstance().getReference().child("artists");
//        databaseInsertHelper(new Lyrics("Test Artist", "Barra Barra",
//                new UserEdit(null, Lyrics.TranslationType.MACHINE, Locale.getDefault().getLanguage(),
//                        "Barra Barra", translation, lines, lyrics, "arabic")));
        artists.child(query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Pair<Lyrics, String>> songs =
                        (Map<String, Pair<Lyrics, String>>) dataSnapshot.getValue(Map.class);
                for (Map.Entry<String, Pair<Lyrics, String>> p : songs.entrySet()) {
                    Log.d("testSearch", p.getKey() + " " + p.getValue().first + " " + p.getValue().second);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("testSearch", databaseError.getMessage());
            }
        });
    }

    @Test
    public void testTranslation() throws Exception {
        TranslationLoader loader = new TranslationLoader(getTargetContext(), "Be or not to be\nThat is the question", "ru");
        loader.loadInBackground();
    }

    @Test
    public void showLocales() throws Exception {
        for (Locale locale : Locale.getAvailableLocales()) {
            Log.d("language", locale.getDisplayLanguage());
        }
    }
}
