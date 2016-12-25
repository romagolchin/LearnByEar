package olegkuro.learnbyear.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.loaders.search.SearchResult;
import olegkuro.learnbyear.model.Lyrics;
import olegkuro.learnbyear.utils.CommonUtils;
import olegkuro.learnbyear.utils.NetworkUtils;


/**
 * Created by Roman on 11/12/2016.
 * now azlyrics is used
 */

public class HTMLLyricsParser {
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "http://search.azlyrics.com/search.php";

    public void setContext(Context context) {
        this.context = context;
    }

    Context context;


    @NonNull
    public LoadResult<List<SearchResult>> search(String query) {
        final StethoURLConnectionManager manager = new StethoURLConnectionManager("LearnByEar");
        LoadResult.ResultType type = LoadResult.ResultType.UNKNOWN_ERROR;
        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
        List<SearchResult> searchResults = new ArrayList<>();

        try {
            builder.appendQueryParameter("q", URLEncoder.encode(query, "UTF-8").replace("+", " "));
        } catch (UnsupportedEncodingException e) {
        }
        if (NetworkUtils.isConnectionAvailable(context)) {
            try {
                String url = builder.build().toString();
                Log.d(TAG + " request", url);
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                Elements panels = doc.getElementsByClass("panel");
                for (Element panel : panels) {
                    Element heading = panel.getElementsByClass("panel-heading").first();
                    String header = heading.getElementsByTag("b").first().ownText();
                    if (header != null && header.toLowerCase().contains("song")) {
                        Elements songs = panel.getElementsByClass("text-left");
                        for (Element song : songs) {
                            Element link = song.getElementsByTag("a").first();
                            String songUrl = link.attr("href");
                            String title = link.text();
                            String artist = song.getElementsByTag("b").get(1).ownText();
                            searchResults.add(new SearchResult(title, CommonUtils.capitalize(artist), new URL(songUrl)));
                        }
                    }
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

    public Lyrics parse(String url) {
//        try {
//            WebClient client = new WebClient();
//            WebRequest request = new WebRequest(new URL(url));
//            HtmlPage page = client.getPage(request);
//            client.getOptions().setJavaScriptEnabled(true);
//            DomElement content = page.getElementById("content-area");
//            DomElement translationArea = content.getFirstByXPath("//div[@class=translate-node-text]");
//            DomElement info = translationArea.getFirstByXPath("//div[@class=info-line-left]");
//            String translationLanguage = info.getTextContent();
//            Log.d(TAG + " translationLanguage", translationLanguage);
//            int index = -1;
//            for (int i = 0; i < translationLanguage.length(); ++i) {
//                Character c = translationLanguage.charAt(i);
//                if (Character.isUpperCase(c))
//                    index = i;
//            }
//            if (index >= 0)
//                translationLanguage = translationLanguage.substring(index);
//
//            String translatedTitle = ((DomElement) translationArea.getFirstByXPath("//h2[@class=title-h2]")).getTextContent();
//            ArrayList<String> translation = new ArrayList<>();
//            for (DomElement line : (List<DomElement>) (((DomElement) translationArea).getByXPath("par"))) {
//                translation.add(line.getTextContent());
//            }
//            DomElement lyricsArea = content.getFirstByXPath("//div[@class=song-node-text]");
//            String language = ( (DomElement) lyricsArea.getFirstByXPath("//div[@class=info-line-left]")).getTextContent();
//            String title = ((DomElement) lyricsArea.getFirstByXPath("//h2[@class=title-h2]")).getTextContent();
//            ArrayList<String> lyrics = new ArrayList<>();
//            for (DomElement line : (List<DomElement>) lyricsArea.getByXPath("par")) {
//                lyrics.add(line.getTextContent());
//            }
//            return new Lyrics(language, null, lyrics, title,
//                    translatedTitle, translation, translationLanguage, Lyrics.TranslationType.HUMAN);
//        } catch (IOException e) {
//            Log.d(TAG, "", e);
//        }
        return null;
    }
}
