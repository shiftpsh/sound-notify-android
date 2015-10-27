package net.shiftstudios.soundnotify.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.shiftstudios.soundnotify.MainActivity;
import net.shiftstudios.soundnotify.R;
import net.shiftstudios.soundnotify.app.AppAdapter;
import net.shiftstudios.soundnotify.app.AppComparator$Alphabetical;
import net.shiftstudios.soundnotify.app.Application;
import net.shiftstudios.soundnotify.helper.AppIconHelper;
import net.shiftstudios.soundnotify.helper.PreferencesHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppEnablingFragment extends Fragment {

    public PreferencesHelper preferencesHelper;
    public AppIconHelper iconHelper;
    public RecyclerView appList;

    public AppEnablingFragment() {
    }

    public static Fragment newInstance() {
        AppEnablingFragment mFrgment = new AppEnablingFragment();
        return mFrgment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_enabling, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View parent = getView();

        preferencesHelper = new PreferencesHelper(getActivity());
        iconHelper = new AppIconHelper(getActivity());

        appList = (RecyclerView) parent.findViewById(R.id.appList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        appList.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Application> apps = getAllInstalledApps();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            appList.setAdapter(new AppAdapter(apps, getActivity()));
                            MainActivity.setLoading(false);
                        }
                    });
                }
            }
        }).start();
    }

    private List<Application> getAllInstalledApps(){
        List<Application> appList = new ArrayList<>();

        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            String packagename = packageInfo.packageName;
            String appName = pm.getApplicationLabel(packageInfo).toString();
            Drawable appIcon = iconHelper.loadIcon(packagename);

            appList.add(new Application(appIcon, appName, packagename));
        }

        Collections.sort(appList, new AppComparator$Alphabetical());

        return appList;
    }
}
