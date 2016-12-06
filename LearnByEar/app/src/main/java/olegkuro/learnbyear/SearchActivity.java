package olegkuro.learnbyear;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Елена on 07.12.2016.
 */

public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putAll(putsmth);
    }
}
