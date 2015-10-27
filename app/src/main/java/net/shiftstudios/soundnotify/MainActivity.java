package net.shiftstudios.soundnotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.shiftstudios.soundnotify.fragment.AppEnablingFragment;
import net.shiftstudios.soundnotify.fragment.TtsSettingsFragment;
import net.shiftstudios.soundnotify.service.NotificationService;


public class MainActivity extends ActionBarActivity {

    public ViewPager viewPager;
    public ViewPagerAdapter adapter;

    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (!NotificationService.isNotificationAccessEnabled) {
            Toast.makeText(this, "알림 정보를 받아올 수 없습니다. 알림 정보를 받아올 수 있도록 \'소리알림\'에 체크해 주세요.", Toast.LENGTH_LONG).show();
            openAccessibilityIntent();
        }
    }

    public void openAccessibilityIntent(){
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    public static void setLoading(boolean isLoading){
        if (isLoading) {
            AlphaAnimation a = new AlphaAnimation(0.0f, 1.0f);
            a.setDuration(1000);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(a);
        } else {
            AlphaAnimation a = new AlphaAnimation(1.0f, 0.0f);
            a.setDuration(1000);
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.startAnimation(a);
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AppEnablingFragment.newInstance();
                case 1:
                    return TtsSettingsFragment.newInstance();
                default:
                    return AppEnablingFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "알림을 받는 앱 설정";
                case 1:
                    return "읽어주기 설정";
                default:
                    return "null";
            }
        }
    }
}
