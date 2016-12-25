package olegkuro.learnbyear;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Roman on 25/12/2016.
 */

public class LearnByEarApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
