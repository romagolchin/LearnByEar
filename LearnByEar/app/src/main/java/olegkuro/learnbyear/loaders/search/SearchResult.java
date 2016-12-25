package olegkuro.learnbyear.loaders.search;

import com.google.firebase.database.DatabaseReference;

import java.net.URL;

/**
 * Created by Roman on 12/12/2016.
 */
public class SearchResult {
    public String title;
    public String artist;
    public URL url;
    // not null if some user edited translation
    public String author;
    public DatabaseReference reference;

    public SearchResult(String title, String artist, URL url) {
        this.title = title;
        this.artist = artist;
        this.url = url;
    }
}
