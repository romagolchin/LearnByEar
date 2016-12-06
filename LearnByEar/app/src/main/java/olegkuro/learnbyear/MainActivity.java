package olegkuro.learnbyear;

//import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Елена on 29.11.2016.
 */

public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //outState.putAll();
        //TODO put identifiers here
    }
}
