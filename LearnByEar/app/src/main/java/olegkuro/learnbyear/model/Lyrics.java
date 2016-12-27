package olegkuro.learnbyear.model;

import java.io.Serializable;

/**
 * Created by Roman on 01/12/2016.
 */

public class Lyrics implements Serializable{
    public enum TranslationType{
        HUMAN,
        MACHINE
    }
    public String title;
    public String translatedTitle;
    public String language;
    public String translationLanguage;
    public String artist;
    public String lyrics;
    public String translation;
    public TranslationType typeOfTranslation;
    public String user;
    public Lyrics(String language, String artist, String lyrics, String title,
                  String translatedTitle, String translation,
                  String translationLanguage, TranslationType typeOfTranslation, String user) {
        this.language = language;
        this.artist = artist;
        this.lyrics = lyrics;
        this.title = title;
        this.translatedTitle = translatedTitle;
        this.translation = translation;
        this.translationLanguage = translationLanguage;
        this.typeOfTranslation = typeOfTranslation;
        this.user = null;
    }

    public Lyrics(String lyrics){
        this.lyrics = lyrics;
    }
}
