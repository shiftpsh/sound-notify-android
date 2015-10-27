package net.shiftstudios.soundnotify.app;

import java.util.Comparator;

/**
 * Created by shiftpsh on 2015-07-18.
 */
public class AppComparator$Alphabetical implements Comparator<Application> {
    @Override
    public int compare(Application app1, Application app2) {
        if (app1.appName != null && app2.appName != null) {
            return (app1.appName).compareTo(app2.appName);
        } else if (app1.appName == null) {
            return 1;
        } else {
            return -1;
        }
    }
}
