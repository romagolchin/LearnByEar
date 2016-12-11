package olegkuro.learnbyear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Елена on 29.11.2016.
 */

public class MainActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(this, SongActivity.class));
        setContentView(R.layout.main_layout);
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //outState.putAll();
        //TODO put identifiers here
    }



}
