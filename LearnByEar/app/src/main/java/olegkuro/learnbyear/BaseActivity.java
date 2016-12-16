package olegkuro.learnbyear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //if you'd like u may change if -> switch
        if (id == R.id.search_song){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true; //poisk pesni sobsna
        }

        if (id == R.id.language){
            return true;
        }

        //show Authors
        if (id == R.id.authors){
            return true;
        }

        //Exit
        if (id == R.id.exit){
            return true;
        }

        if (id == R.id.login) {
            startActivity(new Intent(this, AuthenticationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
