package olegkuro.learnbyear.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import olegkuro.learnbyear.BaseActivity;
import olegkuro.learnbyear.R;
import olegkuro.learnbyear.SearchActivity;
import olegkuro.learnbyear.SongActivity;

import static com.firebase.ui.auth.ui.ResultCodes.RESULT_NO_NETWORK;

/**
 * Created by Roman on 09/12/2016.
 */
public class AuthenticationActivity extends BaseActivity {
    public static boolean isSignedIn = false;
    public static final int RC_SIGNIN = 100;
    public static final String signedInAction = "olegkuro.learnbyear.auth.AuthenticationActivity.action_signed_in";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setProviders(Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                .setLogo(R.drawable.firebase_auth_120dp).build(), RC_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("test", "RESULT");
        Log.d("test", new Integer(resultCode).toString());
        //Suddenly (data == null) if authorized via google
        // google - RESULTCANCELLED
        if (data == null) return;
        Log.d("test", data.toString());
        switch(resultCode) {
            case RESULT_OK: {
                Intent intent = new Intent(this, SignedInActivity.class);
                startActivity(intent);
                isSignedIn = true;
                finish();
                break;
            }
            case RESULT_CANCELED: {
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.sign_in_cancelled), Snackbar.LENGTH_LONG);
            }
            case RESULT_NO_NETWORK: {
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.error_no_network), Snackbar.LENGTH_LONG);
            }
        }
    }
}
