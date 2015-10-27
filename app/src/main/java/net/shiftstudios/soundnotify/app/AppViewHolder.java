package net.shiftstudios.soundnotify.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import net.shiftstudios.soundnotify.R;

/**
 * Created by shiftpsh on 2015-07-18.
 */
public class AppViewHolder extends RecyclerView.ViewHolder {

    public ImageView appIconView;
    public TextView appNameView;
    public CheckBox checkBox;

    public AppViewHolder(View itemView) {
        super(itemView);

        appIconView = (ImageView) itemView.findViewById(R.id.appIconView);
        appNameView = (TextView) itemView.findViewById(R.id.appNameView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
    }
}