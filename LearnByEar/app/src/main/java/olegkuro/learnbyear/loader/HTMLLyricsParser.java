package olegkuro.learnbyear.loader;

import android.net.Uri;
import android.util.Log;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import olegkuro.learnbyear.model.Lyrics;


/**
 * Created by Roman on 11/12/2016.
 */

public class HTMLLyricsParser {
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "http://lyricstranslate.com/";
    private boolean found = false;


    public ArrayList<SearchResult> search(String query, List<String> langCodesTo) {
        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<SearchResult> searchResults = new ArrayList<>();
        ArrayList<String> headers;
        for (String langTo : langCodesTo) {
            try {
                builder.appendPath(langTo).appendPath("site-search").appendQueryParameter("query", query)
                        .appendQueryParameter("op", "Search");
                String anchor = "#gsc.tab=0&gsc.q=" + URLEncoder.encode(query, "UTF-8") + "&gsc.page=1";
                String url = builder.build().toString() + anchor;
                Log.d(TAG + " request", url);
                WebClient client = new WebClient();
                WebRequest request = new WebRequest(new URL(url));
                HtmlPage page = client.getPage(request);
                client.getOptions().setJavaScriptEnabled(true);
                for (int i = 0; i < 5; ++i) {
                    int res = client.waitForBackgroundJavaScript(1000);
                }
                List<?> links = page.getByXPath("//a[@class=gs-per-result-labels]");
                Log.d(TAG + " links size", String.valueOf(links.size()));
                String songUrl;
                boolean isLyricsUrl = false;
                for (int i = 0; i < links.size(); ++i) {
                    if (links.get(i) instanceof DomElement) {
                        DomElement link = (DomElement) links.get(i);
                        String title = link.getTextContent();
                        songUrl = link.getAttribute("href");
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
                        searchResults.add(new SearchResult(title, new URL(songUrl)));
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "", e);
            }

        }
        return searchResults;
    }

    public Lyrics parse(String url) {
        try {
            WebClient client = new WebClient();
            WebRequest request = new WebRequest(new URL(url));
            HtmlPage page = client.getPage(request);
            client.getOptions().setJavaScriptEnabled(true);
            DomElement content = page.getElementById("content-area");
            DomElement translationArea = content.getFirstByXPath("//div[@class=translate-node-text]");
            DomElement info = translationArea.getFirstByXPath("//div[@class=info-line-left]");
            String translationLanguage = info.getTextContent();
            Log.d(TAG + " translationLanguage", translationLanguage);
            int index = -1;
            for (int i = 0; i < translationLanguage.length(); ++i) {
                Character c = translationLanguage.charAt(i);
                if (Character.isUpperCase(c))
                    index = i;
            }
            if (index >= 0)
                translationLanguage = translationLanguage.substring(index);
            
            String translatedTitle = ((DomElement) translationArea.getFirstByXPath("//h2[@class=title-h2]")).getTextContent();
            ArrayList<String> translation = new ArrayList<>();
            for (DomElement line : (List<DomElement>) (((DomElement) translationArea).getByXPath("par"))) {
                translation.add(line.getTextContent());
            }
            DomElement lyricsArea = content.getFirstByXPath("//div[@class=song-node-text]");
            String language = ( (DomElement) lyricsArea.getFirstByXPath("//div[@class=info-line-left]")).getTextContent();
            String title = ((DomElement) lyricsArea.getFirstByXPath("//h2[@class=title-h2]")).getTextContent();
            ArrayList<String> lyrics = new ArrayList<>();
            for (DomElement line : (List<DomElement>) lyricsArea.getByXPath("par")) {
                lyrics.add(line.getTextContent());
            }
            return new Lyrics(language, null, lyrics, title,
                    translatedTitle, translation, translationLanguage, Lyrics.TranslationType.HUMAN);
        } catch (IOException e) {
            Log.d(TAG, "", e);
        }
        return null;
    }
}
