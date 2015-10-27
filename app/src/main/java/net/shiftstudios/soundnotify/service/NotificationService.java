package net.shiftstudios.soundnotify.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;

import net.shiftstudios.soundnotify.helper.PreferencesHelper;

public class NotificationService extends NotificationListenerService {

    public TextToSpeech tts;
    public AudioManager manager;
    public PreferencesHelper helper;
    public String queue = null;

    public static boolean isNotificationAccessEnabled = false;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        isNotificationAccessEnabled = true;
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        isNotificationAccessEnabled = false;
        return mOnUnbind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        helper = new PreferencesHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packagename = sbn.getPackageName();

        if (helper.isNotificationTtsEnabled(packagename) &&
                (!helper.isHeadsetNeeded() | isHeadphonesConnected())) {
            Notification n = sbn.getNotification();
            Bundle extras = n.extras;

            StringBuilder builder = new StringBuilder();
            String appName = getAppName(packagename);
            String title = extras.getString("android.title");
            builder.append(appName);
            builder.append('\n');
            if (title != null)
                builder.append(title.startsWith(appName) ? title.replaceFirst(appName, "") : title);
            builder.append('\n');
            CharSequence textSequence = extras.getCharSequence("android.text");
            if (textSequence != null) builder.append(textSequence);

            speak(builder.toString());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    public boolean isHeadphonesConnected() {
        if (manager != null) return manager.isWiredHeadsetOn();
        else return false;
    }

    public void speak(String s) {
        if (tts != null && !s.equals(queue)) {
            tts.setSpeechRate(helper.getRate());
            tts.setPitch(helper.getPitch());
            tts.speak(s, TextToSpeech.QUEUE_ADD, null);
            queue = s;
        }
    }

    private String getAppName(String packagename) {
        final PackageManager pm = getApplicationContext().getPackageManager();

        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packagename, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }

        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }
}
