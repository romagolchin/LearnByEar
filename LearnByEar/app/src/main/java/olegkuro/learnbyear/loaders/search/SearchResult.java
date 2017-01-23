package olegkuro.learnbyear.loaders.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;

import olegkuro.learnbyear.model.Lyrics;

import static olegkuro.learnbyear.model.Lyrics.HUMAN;

/**
 * Created by Roman on 12/12/2016.
 */
public class SearchResult implements Parcelable {
    /**
     * true when song with the same artist and title is in DB (this instance is
     * not necessarily from DB)
     */
    public boolean presentInDB;
    public String title;
    public String artist;
    public URL url;
    // not null if some user edited translatedText
    public String author;
    public DatabaseReference reference;
    public String langTo;
    public String langFrom;
    @Lyrics.TranslationType
    public int translationType;

    private SearchResult(Parcel parcel) {
        artist = parcel.readString();
        title = parcel.readString();
        author = parcel.readString();
        langFrom = parcel.readString();
        langTo = parcel.readString();

        try {
            url = new URL(parcel.readString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            URL refUrl = new URL(parcel.readString());
            reference = FirebaseDatabase.getInstance().getReference().child(refUrl.getPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int res = parcel.readInt();
        translationType = Lyrics.MACHINE;
        if (res == HUMAN)
            translationType = HUMAN;
        presentInDB = parcel.readInt() != 0;
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel parcel) {
            return new SearchResult(parcel);
        }

        @Override
        public SearchResult[] newArray(int i) {
            return new SearchResult[i];
        }
    };

    public SearchResult(String title, String artist, URL url) {
        this.title = title;
        this.artist = artist;
        this.url = url;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artist);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(langFrom);
        parcel.writeString(langTo);
        parcel.writeString(url == null ? null : url.toString());
        parcel.writeString(reference == null ? null : reference.toString());
        parcel.writeInt(translationType);
        parcel.writeInt(presentInDB ? 1 : 0);
    }
}
