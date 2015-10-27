package net.shiftstudios.soundnotify.app;

import android.graphics.drawable.Drawable;

/**
 * Created by shiftpsh on 2015-07-18.
 */
public class Application {
    public Drawable icon;
    public String appName;
    public String packageName;

    public Application(Drawable icon, String appName, String packageName){
        this.icon = icon;
        this.appName = appName;
        this.packageName = packageName;
    }
}
