package olegkuro.learnbyear.loaders.search;

import com.google.firebase.database.DatabaseReference;

import java.net.URL;

/**
 * Created by Roman on 12/12/2016.
 */
public class SearchResult {
    public String title;
    public String artist;
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

    public SearchResult(int albumId, int trackId, String title, String artist) {
        this.albumId = albumId;
        this.trackId = trackId;
        this.title = title;
        this.artist = artist;
    }
}
