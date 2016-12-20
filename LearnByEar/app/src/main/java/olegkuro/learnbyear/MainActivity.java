package olegkuro.learnbyear;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import olegkuro.learnbyear.auth.AuthenticationActivity;


/**
 * Created by Елена on 29.11.2016.
 */

public class MainActivity extends BaseActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    //private FirebaseRecyclerAdapter<>;
    private String mUsername = "ANONYMOUS";

    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("EXIT", false))
            finish();
        super.onCreate(savedInstanceState);
//         startActivity(new Intent(this, SongActivity.class));
        setContentView(R.layout.main_layout);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //Children entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putAll();
        //TODO put identifiers here
    }


}
