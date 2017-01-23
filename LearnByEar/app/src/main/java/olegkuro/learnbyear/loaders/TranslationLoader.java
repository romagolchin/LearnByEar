package olegkuro.learnbyear.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import olegkuro.learnbyear.loaders.search.LoadResult;
import olegkuro.learnbyear.utils.CommonUtils;
import olegkuro.learnbyear.utils.NetworkUtils;

/**
 * Created by Roman on 27/12/2016.
 */

public class TranslationLoader extends AsyncTaskLoader<LoadResult<String>> {
    private String langTo;
    private String toTranslate;
    private static final String ISSUE_TOKEN_URL = "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
    private static final String TRANSLATE_URL = "https://api.microsofttranslator.com/v2/http.svc/Translate";
    private static final String HEADER_KEY = "Ocp-Apim-Subscription-Key";
    private static final String HEADER_VALUE = "790fe81a0809421ba01d63521bdeb73d";
    private Date date;
    private String mToken;

    public TranslationLoader(Context context, String toTranslate, String langTo) {
        super(context);
        this.toTranslate = toTranslate;
        this.langTo = langTo;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<String> loadInBackground() {
        LoadResult.ResultType type = LoadResult.ResultType.OK;
        String translation = null;
        if (!NetworkUtils.isConnectionAvailable(getContext()))
            type = LoadResult.ResultType.NO_NETWORK;
        else {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(ISSUE_TOKEN_URL).openConnection();
                connection.setRequestProperty(HEADER_KEY, HEADER_VALUE);
                connection.setRequestMethod("POST");
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    throw new BadResponseException("response message " + connection.getResponseMessage());
                if (mToken == null || date == null
                        || (new Date().getTime() - date.getTime() >= 9 * 60 * 1000))
                    mToken = CommonUtils.readToString(connection.getInputStream());
                date = new Date();
                if (mToken == null)
                    throw new BadResponseException("failed to get access token");
                connection.disconnect();
                Uri.Builder builder = Uri.parse(TRANSLATE_URL).buildUpon();
                builder.appendQueryParameter("appid", "Bearer " + mToken).appendQueryParameter("text", toTranslate)
                        .appendQueryParameter("to", langTo);
                connection = (HttpURLConnection) new URL(builder.build().toString()).openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    throw new BadResponseException("response message " + connection.getResponseMessage());
                translation = CommonUtils.readToString(connection.getInputStream());
            } catch (IOException e) {
                Log.e("TranslationLoader", "", e);
            } catch (BadResponseException e) {
                e.printStackTrace();
                type = LoadResult.ResultType.UNKNOWN_ERROR;
            }
        }
        if (translation != null)
            translation = translation.replaceAll("&#xD;", "");
        Log.d("translation", translation != null ? translation : "");
        return new LoadResult<>(translation, type);
    }
}
