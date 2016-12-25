package olegkuro.learnbyear.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.StringTokenizer;


/**
 * Created by Roman on 24/12/2016.
 */

public class CommonUtils {
    private static final String delimiters = " ',:";

    @NonNull
    public static <T> Iterable<T> checkNull(Iterable<T> iterable) {
        if (iterable != null)
            return iterable;
        else
            return Collections.emptyList();
    }

    @Nullable
    public static String capitalize(@Nullable String string) {
        if (string == null) {
            return null;
        }
        string = string.toLowerCase();
        StringTokenizer tokenizer = new StringTokenizer(string, delimiters, true);
        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.isEmpty()) {
                builder.append(Character.toUpperCase(token.charAt(0)));
                if (token.length() >= 1)
                    builder.append(token.substring(1));
            }
        }
        return new String(builder);
    }
}
