package net.shiftstudios.soundnotify.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private Context c;
    private SharedPreferences s;

    public PreferencesHelper(Context c){
        this.c = c;
        this.s = c.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public boolean isNotificationTtsEnabled(String packagename){
        return s.getBoolean("notificationEnabled-" + packagename, true);
    }

    public void setNotificationTtsEnabled(String packagename, boolean enabled){
        SharedPreferences.Editor e = s.edit();
        e.putBoolean("notificationEnabled-" + packagename, enabled);
        e.commit();
    }

    public boolean isHeadsetNeeded(){
        return s.getBoolean("headsetNeeded", true);
    }

    public void setHeadsetNeeded(boolean needed){
        SharedPreferences.Editor e = s.edit();
        e.putBoolean("headsetNeeded", needed);
        e.commit();
    }

    public float getPitch(){
        return s.getFloat("pitch", 1.0f);
    }

    public void setPitch(float f){
        SharedPreferences.Editor e = s.edit();
        e.putFloat("pitch", f);
        e.commit();
    }

    public float getRate(){
        return s.getFloat("rate", 1.0f);
    }

    public void setRate(float f){
        SharedPreferences.Editor e = s.edit();
        e.putFloat("rate", f);
        e.commit();
    }
}
