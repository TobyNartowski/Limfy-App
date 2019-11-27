package pl.tobynartowski.limfy;

import android.app.Application;
import android.content.Context;

public class Limfy extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    private static Application getApplication() {
        return application;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }
}
