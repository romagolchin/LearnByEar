package olegkuro.learnbyear.model;

import java.util.List;

/**
 * Created by Roman on 19/12/2016.
 */

public class User {
    int id;
    String displayName;
    String email;
    // <= 5.0
    float rating;
    List<String> nativeLanguages;
    List<String> languagesToStudy;

    @Override
    public String toString() {
        return Integer.toString(id) + " " + displayName;
    }
}
