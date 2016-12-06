package olegkuro.learnbyear;

//import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    public boolean onCreateOptionsMenu(Menu menu){
        // adds menu items to the action bar if it presents
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //TODO menu event handlers
        //Change Language
        if (id == R.id.action_item1){
            return true;
        }

        //show Authors
        if (id == R.id.action_item2){
            return true;
        }

        //Exit
        if (id == R.id.action_item3){
            return true;
        }

        //delete it later pls
        return true;
    }

}
