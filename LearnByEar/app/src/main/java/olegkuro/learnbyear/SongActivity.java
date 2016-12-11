package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Елена on 07.12.2016.
 */

public class SongActivity extends BaseActivity implements Button.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edit_button) {
            if (!AuthenticationActivity.isSignedIn)
                startActivity(new Intent(this, AuthenticationActivity.class));
            else {
                //TODO edit
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_view_layout);
        Button editButton = (Button) findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "logged in");
        } else
            Log.d(TAG, "not logged in");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
