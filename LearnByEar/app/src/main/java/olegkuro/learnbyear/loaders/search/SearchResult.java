package olegkuro.learnbyear.loaders.search;

import com.google.firebase.database.DatabaseReference;

import java.net.URL;

/**
 * Created by Roman on 12/12/2016.
 */
public class SearchResult {
    public String title;
    /**
     * at <a href="http://lyricstranslate.com></a>
     */
    public int trackId;
    public int albumId;
    public URL url;
    // not null if some user edited translation
    public String author;
    public DatabaseReference reference;

    public SearchResult(String title, URL url) {
        this.title = title;
        this.url = url;
    }

    public SearchResult(int albumId, int trackId, String title) {
        this.albumId = albumId;
        this.trackId = trackId;
        this.title = title;
    }
}
