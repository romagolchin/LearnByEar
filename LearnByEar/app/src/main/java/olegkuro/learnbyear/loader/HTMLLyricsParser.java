package olegkuro.learnbyear.loader;

import android.net.Uri;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
//                WebDriver driver = new FirefoxDriver();
//                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//                driver.get(url);
//                List<WebElement> links = driver.findElements(By.className("gs-per-result-labels"));
//                Document doc = Jsoup.connect(url).get();
//                String all = doc.html();
//                System.out.println(all);
//                System.out.println(all.indexOf("gs-per-result-labels"));
//                Elements links = doc.getElementsByClass("gs-per-result-labels");
//                Element content = doc.getElementById("content");
//                Log.d(TAG, String.valueOf(links.size()));
//                if (links.size() == 0) {
//                    return searchResults;
//                }
//                Elements headerElements = doc.getElementsByClass("gs-title");
//                Log.d(TAG, String.valueOf(headerElements.size()));
                String songUrl;
                boolean isLyricsUrl = false;
//                for (int i = 0; i < links.size(); ++i) {
//                    WebElement link = links.get(i);
//                    songUrl = link.getAttribute("url");
                    // assume it's a page of a musician
//                    if (songUrl.substring(songUrl.length() - 12, 12).equals("-lyrics.html"))
//                        continue;
//                    int cnt = 0;
//                    String path = new URL(songUrl).getPath();
//                    for (int j = 0; j < path.length(); ++j) {
//                        char c = path.charAt(j);
//                        if (c == '/')
//                            ++cnt;
//                    }
//                    if (cnt != 1)
//                        continue;
//                    found = true;
//                    searchResults.add(new SearchResult(headerElements.get(i).ownText(), new URL(songUrl)));
//                }
            } catch (Exception e) {
                Log.d(TAG, "", e);
            }

        }
        return searchResults;
    }

    public Lyrics parse(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element content = doc.getElementById("content-area");
            Element translationArea = content.getElementsByClass("translate-node-text").first();
            String translationLanguage = translationArea.getElementsByClass("info-line").first()
                    .getElementsByClass("info-line-left").first().ownText();
            Log.d(TAG + " translationLanguage", translationLanguage);
            int index = -1;
            for (int i = 0; i < translationLanguage.length(); ++i) {
                Character c = translationLanguage.charAt(i);
                if (Character.isUpperCase(c))
                    index = i;
            }
            if (index >= 0)
                translationLanguage = translationLanguage.substring(index);
            String translatedTitle = translationArea.getElementsByClass("title-h2").first().ownText();
            ArrayList<String> translation = new ArrayList<>();
            for (Element line : translationArea.getElementsByClass("par")) {
                translation.add(line.ownText());
            }
            Element lyricsArea = content.getElementsByClass("song-node-text").first();
            String language = lyricsArea.getElementsByClass("info-line-left").first().ownText();
            String title = lyricsArea.getElementsByClass("title-h2").first().ownText();
            ArrayList<String> lyrics = new ArrayList<>();
            for (Element line : lyricsArea.getElementsByClass("par")) {
                lyrics.add(line.ownText());
            }
            return new Lyrics(language, null, lyrics, title,
                    translatedTitle, translation, translationLanguage, Lyrics.TranslationType.HUMAN);
        } catch (IOException e) {
            Log.d(TAG, "", e);
        }
        return null;
    }
}
