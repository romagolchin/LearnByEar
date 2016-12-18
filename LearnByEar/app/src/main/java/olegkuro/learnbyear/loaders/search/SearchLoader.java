package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 12/12/2016.
 * Tries to get lyrics from 3 sources: database, <a href="http://lyricstranslate.com"></a>
 * and <a href="http://azlyrics.com"></a>
 */

public class SearchLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private LoadResult<List<SearchResult>> result = null;
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "http://lyricstranslate.com/";
    private boolean found = false;
    private String query;
    private List<String> langCodesTo;

    public SearchLoader(Context context, Bundle args) {
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
        List<String> urls = new ArrayList<>();
        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
        // TODO search in database and azlyrics
        List<SearchResult> searchResults = new ArrayList<>();
//        for (String langTo : langCodesTo) {
//            try {
//                builder.appendPath(langTo).appendPath("site-search").appendQueryParameter("query", query)
//                        .appendQueryParameter("op", "Search");
//                String anchor = "#gsc.tab=0&gsc.q=" + URLEncoder.encode(query, "UTF-8") + "&gsc.page=1";
//                String url = builder.build().toString() + anchor;
//                Log.d(TAG + " request", url);
//                WebClient client = new WebClient();
//                WebRequest request = new WebRequest(new URL(url));
//                HtmlPage page = client.getPage(request);
//                client.getOptions().setJavaScriptEnabled(true);
//                for (int i = 0; i < 5; ++i) {
//                    int res = client.waitForBackgroundJavaScript(1000);
//                }
//                List<?> links = page.getByXPath("//a[@class=gs-per-result-labels]");
//                Log.d(TAG + " links size", String.valueOf(links.size()));
//                String songUrl;
//                boolean isLyricsUrl = false;
//                for (int i = 0; i < links.size(); ++i) {
//                    if (links.get(i) instanceof DomElement) {
//                        DomElement link = (DomElement) links.get(i);
//                        String title = link.getTextContent();
//                        songUrl = link.getAttribute("href");
//                        // assume it's a page of a musician
//                        if (songUrl.substring(songUrl.length() - 12, 12).equals("-lyrics.html"))
//                            continue;
//                        int cnt = 0;
//                        String path = new URL(songUrl).getPath();
//                        for (int j = 0; j < path.length(); ++j) {
//                            char c = path.charAt(j);
//                            if (c == '/')
//                                ++cnt;
//                        }
//                        if (cnt != 1)
//                            continue;
//                        found = true;
//                        searchResults.add(new SearchResult(title, new URL(songUrl)));
//                    }
//                }
//            } catch (Exception e) {
//                Log.d(TAG, "", e);
//            }
//
//        }
        return new LoadResult<>(searchResults, LoadResult.ResultType.OK);
    }
}
