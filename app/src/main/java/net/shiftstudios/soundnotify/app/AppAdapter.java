package net.shiftstudios.soundnotify.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import net.shiftstudios.soundnotify.R;
import net.shiftstudios.soundnotify.helper.PreferencesHelper;

import java.util.List;

/**
 * Created by shiftpsh on 2015-07-18.
 */
public class AppAdapter extends RecyclerView.Adapter {

    private List<Application> appList;
    private Context c;
    private PreferencesHelper helper;

    public AppAdapter(List<Application> appList, Context c) {
        this.appList = appList;
        this.c = c;
        this.helper = new PreferencesHelper(c);
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app, viewGroup, false);
        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppViewHolder viewHolder = (AppViewHolder) holder;
        Application item = appList.get(position);

        Log.d(item.packageName, helper.isNotificationTtsEnabled(item.packageName) + "");

        viewHolder.appIconView.setImageDrawable(item.icon);
        viewHolder.appNameView.setText(item.appName);
        viewHolder.checkBox.setChecked(helper.isNotificationTtsEnabled(item.packageName));

        viewHolder.checkBox.setTag(item.packageName);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    helper.setNotificationTtsEnabled((String) compoundButton.getTag(), b);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }
}
