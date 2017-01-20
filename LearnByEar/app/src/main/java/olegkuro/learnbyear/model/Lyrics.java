package olegkuro.learnbyear.model;

import android.support.annotation.IntDef;

import java.io.Serializable;

/**
 * Created by Roman on 01/12/2016.
 * brief info about lyrics
 * used in the index/* paths in DB
 */

public class Lyrics implements Serializable {
    @IntDef({MACHINE, HUMAN})
    public @interface TranslationType {}
    public static final int MACHINE = 0;
    public static final int HUMAN = 1;


    public UserEdit userEdit;
    public String artist;
    public String title;


    public Lyrics(String artist, String title, UserEdit userEdit) {
        this.artist = artist;
        this.title = title;
        this.userEdit = userEdit;
    }

}
