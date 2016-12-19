package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import olegkuro.learnbyear.NetworkUtils;

/**
 * Created by Roman on 12/12/2016.
 */

public class SearchLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private LoadResult<List<SearchResult>> result = null;
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "https://music.yandex.ru/handlers/music-search.jsx";
    private boolean found = false;
    private String request;
    private List<String> langCodesTo;

    public SearchLoader(Context context, Bundle args) {
        super(context);
        this.request = args.getString("request");
        this.langCodesTo = args.getStringArrayList("langCodesTo");
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        } else
            forceLoad();
    }

    @Override
    public LoadResult<List<SearchResult>> loadInBackground() {
        List<String> urls = new ArrayList<>();
        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
        List<SearchResult> searchResults = new ArrayList<>();
        HttpURLConnection connection;
        if (NetworkUtils.isConnectionAvailable(getContext())) {
            try {
                URL url = new URL(builder.appendQueryParameter("text", URLEncoder.encode(request, "UTF-8"))
                        .appendQueryParameter("type", "all")
                        .appendQueryParameter("lang", "ru")
                        .appendQueryParameter("external-domain", "music.yandex.ru")
                        .appendQueryParameter("overembed", "false").build().toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    String content;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int size;
                    while ((size = is.read(buffer)) >= 0) {
                        baos.write(buffer, 0, size);
                    }
                    content = new String(baos.toByteArray(), "UTF-8");
                    JSONObject root = new JSONObject(content);
                    JSONObject tracks = root.getJSONObject("tracks");
                    JSONArray tracksItems = tracks.getJSONArray("items");

                    for (int j = 0; j < tracksItems.length(); ++j) {
                        JSONObject track = tracksItems.getJSONObject(j);
                        int trackId = track.getInt("id");
                        int albumId = track.getJSONArray("albums").getJSONObject(0).getInt("id");
                        searchResults.add(new SearchResult(albumId, trackId, track.getString("title")));
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "", e);
                return new LoadResult<>(null, LoadResult.ResultType.UNKNOWN_ERROR);
            }
        } else {
            return new LoadResult<>(null, LoadResult.ResultType.NO_NETWORK);
        }
        if (searchResults.size() != 0)
            return new LoadResult<>(searchResults, LoadResult.ResultType.OK);
        else
            return new LoadResult<>(searchResults, LoadResult.ResultType.EMPTY);
    }
}
