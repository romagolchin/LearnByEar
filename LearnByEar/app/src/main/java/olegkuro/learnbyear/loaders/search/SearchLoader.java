package olegkuro.learnbyear.loaders.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Roman on 12/12/2016.
 */

public class SearchLoader extends AsyncTaskLoader<LoadResult<List<SearchResult>>> {
    private LoadResult<List<SearchResult>> result;
    private final String TAG = getClass().getSimpleName();
    private static final String BASE_URI = "https://music.yandex.ru/handlers/music-search.jsx";
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
//        LoadResult.ResultType type = LoadResult.ResultType.UNKNOWN_ERROR;
//        Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
//        List<SearchResult> searchResults = new ArrayList<>();
//        HttpURLConnection connection;
//        if (NetworkUtils.isConnectionAvailable(getContext())) {
//            try {
//                URL url = new URL(builder.appendQueryParameter("text", URLEncoder.encode(request, "UTF-8"))
//                        .appendQueryParameter("type", "all")
//                        .build().toString());
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("Cookie",
//                        "fuid01=56a1449d0ddbc2ca.UwV4-VyZGkNQz9q7WUcDDXPkjzdE_ulIZP_9rVThP_50y4F0J9cz3COksZVQ7gydPG6ythnB5A_rIPsq_a8ZFQJlbFwie9Dg1ZdD_pjw3AEArtJVm2bg5ZZ2IOExNA8y; yandexuid=2270118161451570630; _ym_uid=1471717701233586862; i=50upzurKWMVq0qnVrd+g8MNNzLhR9i0Nva5iQZzUgnziyerI7jY4e6HXtd4wbM5ezZjQz6pL56WtXKhQXWeb10D9qQ==; yandex_gid=2; yabs-frequency=/4/1m0206myxbTOFKzN/sInoS6mdBjWiSd1i9o7dGh1mR2Sd/; yp=1496177962.dswa.0#1496177962.dsws.16#1495912800.dwss.6#1496177962.dwys.8#1783153353.multib.1#1492902732.st_browser_cl.1#1496181148.st_browser_s.3#1496133056.szm.1_00%3A1366x768%3A1366x638#1482957050.ygu.1#1488141054.ww.1#1482957243.los.1#1795776839.udn.cDpnb2w0aW4ucm9tYW4%3D; L=XXsBBml5VXl2AFNxUFd6ZUhbTVVrVG1jVCYlcjxYWScMIDYd.1480416839.12799.332685.d42f8b032bb81c22a7fc0b320d73c724; yandex_login=gol4in.roman; sessionid2=3:1480769948.5.0.1480416839751:2q5kXA:cf.1|101672638.0.2|155401.895414.jh2QP9ocHbGvpJ5KvTKc96yh4MM; Session_id=3:1481626601.5.0.1480416839751:2q5kXA:cf.0|101672638.0.2|155877.122846.LNCxLn4YYfjnWPVawBbca-IPMvI; device_id=\"a2163401dd5c32e24b1e02b66a0a59b9ce7e4adbc\"; _ym_isad=1; _ym_visorc_1028356=b; lastVisitedPage=%7B%22101672638%22%3A%22%2Falbum%2F3487314%2Ftrack%2F28905008%22%7D; yabs-vdrf=Bh5TO3W3P60000; fuid01=56a1449d0ddbc2ca.UwV4-VyZGkNQz9q7WUcDDXPkjzdE_ulIZP_9rVThP_50y4F0J9cz3COksZVQ7gydPG6ythnB5A_rIPsq_a8ZFQJlbFwie9Dg1ZdD_pjw3AEArtJVm2bg5ZZ2IOExNA8y; yandexuid=2270118161451570630; _ym_uid=1471717701233586862; i=50upzurKWMVq0qnVrd+g8MNNzLhR9i0Nva5iQZzUgnziyerI7jY4e6HXtd4wbM5ezZjQz6pL56WtXKhQXWeb10D9qQ==; yandex_gid=2; yabs-frequency=/4/1m0206myxbTOFKzN/sInoS6mdBjWiSd1i9o7dGh1mR2Sd/; yp=1496177962.dswa.0#1496177962.dsws.16#1495912800.dwss.6#1496177962.dwys.8#1783153353.multib.1#1492902732.st_browser_cl.1#1496181148.st_browser_s.3#1496133056.szm.1_00%3A1366x768%3A1366x638#1482957050.ygu.1#1488141054.ww.1#1482957243.los.1#1795776839.udn.cDpnb2w0aW4ucm9tYW4%3D; L=XXsBBml5VXl2AFNxUFd6ZUhbTVVrVG1jVCYlcjxYWScMIDYd.1480416839.12799.332685.d42f8b032bb81c22a7fc0b320d73c724; yandex_login=gol4in.roman; sessionid2=3:1480769948.5.0.1480416839751:2q5kXA:cf.1|101672638.0.2|155401.895414.jh2QP9ocHbGvpJ5KvTKc96yh4MM; Session_id=3:1481626601.5.0.1480416839751:2q5kXA:cf.0|101672638.0.2|155877.122846.LNCxLn4YYfjnWPVawBbca-IPMvI; device_id=\"a2163401dd5c32e24b1e02b66a0a59b9ce7e4adbc\"; _ym_isad=1; yabs-vdrf=Bh5TO3W3P60000; lastVisitedPage=%7B%22101672638%22%3A%22%2F%22%7D");
//                connection.connect();
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    InputStream is = connection.getInputStream();
//                    String content;
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[8192];
//                    int size;
//                    while ((size = is.read(buffer)) >= 0) {
//                        baos.write(buffer, 0, size);
//                    }
//                    content = new String(baos.toByteArray(), "UTF-8");
//                    JSONObject root = new JSONObject(content);
//                    JSONObject tracks = root.getJSONObject("tracks");
//                    JSONArray tracksItems = tracks.getJSONArray("items");
//
//                    for (int j = 0; j < tracksItems.length(); ++j) {
//                        JSONObject track = tracksItems.getJSONObject(j);
//                        int trackId = track.getInt("id");
//                        int albumId = track.getJSONArray("albums").getJSONObject(0).getInt("id");
//                        String artist = track.getJSONArray("artists").getJSONObject(0).getString("name");
//                        searchResults.add(new SearchResult(albumId, trackId, track.getString("title"),
//                                artist));
//                    }
//                }
//            } catch (Exception e) {
//                Log.d(TAG, "", e);
//            }
//            if (searchResults.size() != 0) {
//                type = LoadResult.ResultType.OK;
//            }
//        } else {
//            type = LoadResult.ResultType.NO_NETWORK;
//        }
//        result = new LoadResult<>(type != LoadResult.ResultType.OK ? null : searchResults, type);
        return result;
    }
}
