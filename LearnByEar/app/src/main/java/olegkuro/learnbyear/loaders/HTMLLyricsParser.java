package olegkuro.learnbyear.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchResult;
import olegkuro.learnbyear.model.Lyrics;
import olegkuro.learnbyear.model.UserEdit;
import olegkuro.learnbyear.utils.CommonUtils;
import olegkuro.learnbyear.utils.NetworkUtils;


/**
 * Created by Roman on 11/12/2016.
 * now azlyrics is used
 */

public class HTMLLyricsParser {
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "http://search.azlyrics.com/search.php";
    private static final String userAgent = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private static final String referrer = "http://www.google.com";

    public void setContext(Context context) {
        this.context = context;
    }

    Context context;


    @NonNull
    public LoadResult<List<SearchResult>> search(String query) {
        LoadResult.ResultType type = LoadResult.ResultType.UNKNOWN_ERROR;
        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
        List<SearchResult> searchResults = new ArrayList<>();
        try {
            builder.appendQueryParameter("q", URLEncoder.encode(query, "UTF-8").replace("+", " "))
                    .appendQueryParameter("p", "1")
                    .appendQueryParameter("w", "songs");
        } catch (UnsupportedEncodingException e) {
        }
        if (NetworkUtils.isConnectionAvailable(context)) {
            try {
                String url = builder.build().toString();
                Log.d(TAG + " request", url);
                Document doc = Jsoup.connect(url)
                        .userAgent(userAgent)
                        .referrer(referrer)
                        .get();
                Element panel = doc.getElementsByClass("panel").first();
                Elements songs = panel.getElementsByClass("text-left");
                for (Element song : songs) {
                    Element link = song.getElementsByTag("a").first();
                    String songUrl = link.attr("href");
                    String title = link.text();
                    String artist = song.getElementsByTag("b").get(1).ownText();
                    searchResults.add(new SearchResult(title, CommonUtils.capitalize(artist), new URL(songUrl)));
                }
                type = searchResults.isEmpty() ? LoadResult.ResultType.EMPTY : LoadResult.ResultType.OK;
            } catch (Exception e) {
                Log.d(TAG, "", e);
            }
        } else {
            type = LoadResult.ResultType.NO_NETWORK;
        }
        return new LoadResult<>(searchResults, type);

    }


    public LoadResult<UserEdit> parse(String url) {
        Log.d(TAG + " url", url);
        LoadResult.ResultType resultType = LoadResult.ResultType.UNKNOWN_ERROR;
        String lyrics = "";
        List<String> lines = new ArrayList<>();
        try {
            if (NetworkUtils.isConnectionAvailable(context)) {
                Document doc = Jsoup.connect(url)
                        .userAgent(userAgent).referrer(referrer).get();
                Element text = doc.getElementsByClass("main-page").first()
                        .getElementsByClass("row").first()
                        .child(1);
                Elements divs = text.getElementsByTag("div");
                Element div = divs.select("div:not([class])").first();
                String[] lyricsArray = div.html().split("<br>");
                StringBuilder builder = new StringBuilder();
                lines = new ArrayList<>(Arrays.asList(lyricsArray));
                for (int i = 0; i < lyricsArray.length; ++i) {
                    Log.d("", lyricsArray[i]);
                    // exclude html comment
                    int commentStart, commentEnd;
                    if ((commentStart = lyricsArray[i].indexOf("<!--")) >= 0) {
                        commentEnd = lyricsArray[i].indexOf("-->");
                        String withoutComment = lyricsArray[i].substring(Math.max(commentStart + 4, commentEnd + 3));
                        builder.append(withoutComment);
                        lines.set(i, withoutComment);
                    } else {
                        builder.append(lyricsArray[i]);
                    }
                }
                lyrics = new String(builder);
                // exclude HTML tags
                lyrics = lyrics.replaceAll("<[a-z]+>|</[a-z]+>|&amp;", "");
                resultType = lyrics.isEmpty() ? LoadResult.ResultType.EMPTY : LoadResult.ResultType.OK;
            } else {
                resultType = LoadResult.ResultType.NO_NETWORK;
            }
        } catch (IOException e) {
            Log.d(TAG, "", e);
        }
        UserEdit edit = new UserEdit(null, Lyrics.MACHINE, null, null, null, lines, lyrics, null);
        return new LoadResult<>(edit, resultType);
    }
}
