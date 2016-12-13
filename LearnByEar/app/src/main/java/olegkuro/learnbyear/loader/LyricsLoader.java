package olegkuro.learnbyear.loader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 12/12/2016.
 * Tries to get lyrics from 3 sources: database, <a href="http://lyricstranslate.com"></a>
 * and <a href="http://azlyrics.com"></a>
 */

public class LyricsLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private LoadResult<List<SearchResult>> result = null;
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "http://lyricstranslate.com/";
    private boolean found = false;
    private String query;
    private List<String> langCodesTo;

    public LyricsLoader(Context context, Bundle args) {
        super(context);
        this.query = args.getString("query");
        this.langCodesTo = args.getStringArrayList("langCodesTo");
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        } else
            forceLoad();
    }

    @Override
    public LoadResult<List<SearchResult>> loadInBackground() {
        // TODO search in database and azlyrics
        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
        List<String> urls = new ArrayList<>();
        List<SearchResult> searchResults = new ArrayList<>();
        for (String langTo : langCodesTo) {
            builder.appendPath(langTo).appendQueryParameter("query", query)
                    .appendQueryParameter("op", "Search");
            String anchor = "#gsc.tab=0&gsc.q=" + query + "&gsc.page=1";
            try {
                String url = builder.build().toString() + anchor;
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.getElementsByClass("gs-per-result-labels");
                if (links.size() == 0) {
                    return null;
                }
                Elements headerElements = doc.getElementsByClass("gs-title");
                String songUrl;
                boolean isLyricsUrl = false;
                for (int i = 0; i < links.size(); ++i) {
                    Element link = links.get(i);
                    songUrl = link.attr("url");
                    // assume it's a page of a musician
                    if (songUrl.substring(songUrl.length() - 12, 12).equals("-lyrics.html"))
                        continue;
                    int cnt = 0;
                    String path = new URL(songUrl).getPath();
                    for (int j = 0; j < path.length(); ++j) {
                        char c = path.charAt(j);
                        if (c == '/')
                            ++cnt;
                    }
                    if (cnt != 1)
                        continue;
                    found = true;
                    searchResults.add(new SearchResult(headerElements.get(i).ownText(), new URL(songUrl)));
                }
            } catch (Exception e) {
                Log.d(TAG, "", e);
            }

        }
        return new LoadResult<>(searchResults, LoadResult.ResultType.OK);
    }
}
