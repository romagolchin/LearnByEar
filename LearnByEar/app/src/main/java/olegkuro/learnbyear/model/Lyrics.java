package olegkuro.learnbyear.model;

import java.io.Serializable;
import java.util.List;

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
    public List<String> lyrics;
    public List<String> translation;
    public TranslationType typeOfTranslation;
    public String user;
    public Lyrics(String language, String artist, List<String> lyrics, String title,
                  String translatedTitle, List<String> translation,
                  String translationLanguage, TranslationType typeOfTranslation) {
        this.language = language;
        this.artist = artist;
        this.lyrics = lyrics;
        this.title = title;
        this.translatedTitle = translatedTitle;
        this.translation = translation;
        this.translationLanguage = translationLanguage;
        this.typeOfTranslation = typeOfTranslation;
    }

    public Lyrics(String language, String artist, String title){
        this.language = language;
        this.artist = artist;
        this.title = title;
    }
}
