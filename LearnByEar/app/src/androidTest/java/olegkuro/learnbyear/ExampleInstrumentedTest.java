package olegkuro.learnbyear;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import olegkuro.learnbyear.loader.HTMLLyricsParser;
import olegkuro.learnbyear.loader.SearchResult;

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
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("olegkuro.learnbyear", appContext.getPackageName());
    }

    @Test
    public void testHTMLParse() throws Exception {
        HTMLLyricsParser parser = new HTMLLyricsParser();
        List<SearchResult> searchResults = parser.search("bob dylan", Arrays.asList("en"));
        if (searchResults != null) {
            for (SearchResult searchResult : searchResults)
                Log.d(getClass().getSimpleName(), searchResult.toString());
        }
    }
}
