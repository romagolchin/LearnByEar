package olegkuro.learnbyear;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import olegkuro.learnbyear.auth.AuthenticationActivity;
import olegkuro.learnbyear.auth.SignedInActivity;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    //private FirebaseRecyclerAdapter<>;
    private String mUsername = "ANONYMOUS";
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


        //Change Language
        if (id == R.id.search_song){
            if (!SearchActivity.class.equals(getClass())) {
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true; //poisk pesni sobsna
            }
        }

        if (id == R.id.language){
            return true;
        }

        //show Authors
        if (id == R.id.authors){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://github.com/romagolchin/LearnByEar"));
            startActivity(intent);
        }

        //Exit
        if (id == R.id.exit){
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }

        if (id == R.id.login) {
            if (!AuthenticationActivity.class.equals(getClass())) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    startActivity(new Intent(this, SignedInActivity.class));
                }
                Log.d("test", "logging in");
                startActivity(new Intent(this, AuthenticationActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
