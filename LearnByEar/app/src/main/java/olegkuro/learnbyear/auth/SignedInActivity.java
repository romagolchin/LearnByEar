package olegkuro.learnbyear.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import olegkuro.learnbyear.BaseActivity;
import olegkuro.learnbyear.R;

public class SignedInActivity extends BaseActivity implements Button.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_out: {
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignedInActivity.this, AuthenticationActivity.class));
                            finish();
                        } else
                            Snackbar.make(findViewById(R.id.activity_signed_in), getString(R.string.sign_out_fail), Snackbar.LENGTH_LONG);
                    }
                });
                break;
            }
            case R.id.delete_account: {
                AuthUI.getInstance()
                        .delete(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(SignedInActivity.this, AuthenticationActivity.class));
                                    finish();
                                } else {
                                    Snackbar.make(findViewById(R.id.activity_signed_in), getString(R.string.sign_out_fail), Snackbar.LENGTH_LONG);
                                }
                            }
                        });
                break;
            }
        }
    }
}
