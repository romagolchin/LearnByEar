package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Елена on 29.11.2016.
 */

public class MainActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("EXIT", false))
            finish();
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SongActivity.class));
        setContentView(R.layout.main_layout);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putAll();
        //TODO put identifiers here
    }


}
