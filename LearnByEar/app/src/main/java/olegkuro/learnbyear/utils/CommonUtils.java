package olegkuro.learnbyear.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.StringTokenizer;


/**
 * Created by Roman on 24/12/2016.
 */

public class CommonUtils {
    public static final String longDash = " \u2014 ";
    private static final String delimiters = " ',:";
    private static final String firebaseStopChars = ".#$[]";

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

    public static String excludeStopChars(String path) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.length(); ++i) {
            if (!firebaseStopChars.contains(String.valueOf(path.charAt(i))))
                builder.append(path.charAt(i));
        }
        return new String(builder);
    }

    public static @Nullable String readToString(@Nullable InputStream inputStream) throws IOException {
        if (inputStream != null) {
            int readSize;
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((readSize = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, readSize);
            }
            byte[] data = outputStream.toByteArray();
            return new String(data, "UTF-8");
        }
        return null;
    }

}
