package olegkuro.learnbyear;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import olegkuro.learnbyear.loaders.HTMLLyricsParser;
import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchResult;

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
        parser.setContext(InstrumentationRegistry.getTargetContext());
        LoadResult<List<SearchResult>> loadResult = parser.search("bob dylan");
        for (SearchResult searchResult : loadResult.data)
            Log.d(getClass().getSimpleName(), searchResult.toString());
    }
}
