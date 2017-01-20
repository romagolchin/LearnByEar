package olegkuro.learnbyear.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Roman on 11/01/2017.
 */

@IgnoreExtraProperties
public class UserEdit implements Serializable {
    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public String language;
    public String translationLanguage;
    public String translatedTitle;

    public String getLanguage() {
        return language;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getTranslatedTitle() {
        return translatedTitle;
    }

    public String getTranslationLanguage() {
        return translationLanguage;
    }

    public @Lyrics.TranslationType int getTypeOfTranslation() {
        return typeOfTranslation;
    }

    public String getUser() {
        return user;
    }

    public List<String> lines;
    public String lyrics;
    public String translatedText;
    public @Lyrics.TranslationType int typeOfTranslation;
    public String user;

    public UserEdit(String user, @Lyrics.TranslationType int typeOfTranslation, String translationLanguage, String translatedTitle, String translatedText, List<String> lines, String lyrics, String language) {
        this.user = user;
        this.typeOfTranslation = typeOfTranslation;
        this.translationLanguage = translationLanguage;
        this.translatedTitle = translatedTitle;
        this.translatedText = translatedText;
        this.lines = lines;
        this.lyrics = lyrics;
        this.language = language;
    }

    public UserEdit() {
    }


}
